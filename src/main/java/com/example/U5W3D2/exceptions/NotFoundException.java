package com.example.U5W3D2.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException{
    public NotFoundException(UUID id) {
        super("item with " + id + " not found.");
    }
    public NotFoundException(String message) {
        super(message);
    }
}
