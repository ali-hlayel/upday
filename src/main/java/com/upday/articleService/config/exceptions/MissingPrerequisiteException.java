package com.upday.articleService.config.exceptions;

public class MissingPrerequisiteException extends Exception {

    public MissingPrerequisiteException() {
    }

    public MissingPrerequisiteException(String message) {
        super(message);
    }
}
