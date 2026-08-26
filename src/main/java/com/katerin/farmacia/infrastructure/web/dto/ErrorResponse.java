package com.katerin.farmacia.infrastructure.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int code, String message, LocalDateTime timestamp) {
    
}
