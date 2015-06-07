package com.github.bingoohuang.springrestclient.boot.exception;

public class RestException extends RuntimeException {
    private final int httpStatusCode;

    public RestException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public RestException(int httpStatusCode, String message, Throwable cause) {
        super(message, cause);

        this.httpStatusCode = httpStatusCode;
    }


    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
