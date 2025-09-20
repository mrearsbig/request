package com.mrearsbig.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mrearsbig.model.user.User;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String code;
    private String message;
    private User data;
}