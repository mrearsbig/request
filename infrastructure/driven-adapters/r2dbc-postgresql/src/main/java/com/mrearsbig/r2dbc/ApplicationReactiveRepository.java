package com.mrearsbig.r2dbc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mrearsbig.r2dbc.data.ApplicationData;

import reactor.core.publisher.Flux;

// TODO: This file is just an example, you should delete or modify it
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationData, UUID>, ReactiveQueryByExampleExecutor<ApplicationData> {
    @Query("""
        SELECT * 
        FROM application a
        WHERE a.status_id IN (2, 3, 5) 
        LIMIT :size OFFSET :offset
    """)
    Flux<ApplicationData> findAllPendingForReview(int size, int offset);
}
