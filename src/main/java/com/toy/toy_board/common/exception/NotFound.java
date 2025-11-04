package com.toy.toy_board.common.exception;

import org.aspectj.weaver.ast.Not;

public class NotFound extends ClientError {
    public NotFound(String message) {
        this.errorCode = "NotFound";
        this.errorMessage = message;
    }
}
