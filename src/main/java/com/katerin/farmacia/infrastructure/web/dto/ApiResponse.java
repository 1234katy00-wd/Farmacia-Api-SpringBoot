package com.katerin.farmacia.infrastructure.web.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
    int status,
    String message,
    String name,
    LocalDateTime timestamp
    ){
        public static ApiResponse ok(String message, String  name){
            return new ApiResponse(200, message, name, LocalDateTime.now());
        }

        public static ApiResponse ok(String message){
            return new ApiResponse(200, message, null, LocalDateTime.now());
        }

        public static ApiResponse error(int status, String message){
            return new ApiResponse(status, message, null, LocalDateTime.now());
        }
    }
    
