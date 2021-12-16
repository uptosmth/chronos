package com.uptosmth.chronos.api.exception;

import com.linecorp.armeria.common.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException() {
        super("Bad request");
    }

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
