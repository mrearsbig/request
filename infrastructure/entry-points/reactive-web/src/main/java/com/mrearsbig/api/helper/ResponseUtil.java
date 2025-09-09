package com.mrearsbig.api.helper;

import com.mrearsbig.api.dto.Response;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseUtil {
    public static <T> Response<T> responseCreated(T data) {
        return Response.<T>builder()
                .code("REQ_201")
                .message("Successfully registered application")
                .data(data)
                .build();
    }

    public static <T> Response<T> responseError(T data) {
        return Response.<T>builder()
                .code("REQ_401")
                .message("Unauthorized")
                .data(data)
                .build();
    }
}
