package com.order.management.system.orderingmicroservice.util.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class StandardError {

    @CreationTimestamp
    @JsonFormat(pattern = "AAAA-MM-DDTHH:MM:SS", locale = "pt_BR")
    private LocalDateTime time;

    private int statusCode;

    private String error;

    private String message;

    private String path;

    public StandardError(int statusCode, String error, String message, String path) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
