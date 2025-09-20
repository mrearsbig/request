package com.mrearsbig.sqs.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrearsbig.model.application.Application;
import com.mrearsbig.model.application.gateways.NotificationPublisher;
import com.mrearsbig.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements NotificationPublisher {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<Void> publish(Application application) {
        return Mono.fromCallable(() -> {
            try {
                // Serializamos el Application como JSON
                String message = objectMapper.writeValueAsString(application);
                log.info("Publishing Application event to SQS: {}", message);
                return buildRequest(message);
            } catch (Exception e) {
                throw new RuntimeException("Error serializing Application", e);
            }
        })
        .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
        .doOnNext(response -> log.debug("Message sent with ID {}", response.messageId()))
        .then();
    }
}
