package com.toy.toy_board.common.exception;

public class BadParameter extends ClientError {
    public BadParameter(String message) {
        this.errorCode = "BadParameter";
        this.errorMessage = message;
    }
}
