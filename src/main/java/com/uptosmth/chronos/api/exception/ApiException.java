package com.uptosmth.chronos.api.exception;

import java.util.HashMap;
import java.util.Map;

import com.linecorp.armeria.common.HttpStatus;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {
            {
                put("message", getMessage());
            }
        };
    }
}
