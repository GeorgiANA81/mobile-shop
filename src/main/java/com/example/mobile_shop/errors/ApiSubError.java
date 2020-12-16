package com.example.mobile_shop.errors;

interface ApiSubError {

}

class AppValidationError implements  ApiSubError{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public AppValidationError(String object, String message) {
        this.object = object;
        this.message = message;

    }

}
