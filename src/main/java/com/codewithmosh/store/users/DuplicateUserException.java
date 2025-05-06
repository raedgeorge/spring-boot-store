package com.codewithmosh.store.users;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("Email is already registered");
    }
}
