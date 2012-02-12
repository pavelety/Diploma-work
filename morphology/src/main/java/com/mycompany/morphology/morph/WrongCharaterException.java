package com.mycompany.morphology.morph;

public class WrongCharaterException extends RuntimeException {
    public WrongCharaterException() {
    }

    public WrongCharaterException(String message) {
        super(message);
    }
}
