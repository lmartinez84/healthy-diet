package com.github.lmartinez84.healthydiet.user.domain.exceptions;

public class BirthDateNotInPastUserException extends RuntimeException {
    public BirthDateNotInPastUserException() {
        super("User birth date should be in the past");
    }
}
