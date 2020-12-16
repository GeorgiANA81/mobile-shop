package com.example.mobile_shop.errors;

import com.example.mobile_shop.exceptions.EmptyInputException;
import com.example.mobile_shop.exceptions.NegativeInputException;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.example.mobile_shop.exceptions.ResourceNotFoundDeleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        String message = "Malformed request";
        return buildResponseEntity(new ApiError(status, message, ex));

    }

    @ExceptionHandler(ResourceNotFoundDeleteException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ResourceNotFoundDeleteException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);

        apiError.setMessage(ex.getMessage());

        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(NegativeInputException.class)
    protected ResponseEntity<Object> handleNegativeInput(NegativeInputException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);

        apiError.setMessage(ex.getMessage());

        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(EmptyInputException.class)
    protected ResponseEntity<Object> handleEmptyInput(EmptyInputException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);

        apiError.setMessage(ex.getMessage());

        return buildResponseEntity(apiError);

    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError){
        return new ResponseEntity<>(apiError,apiError.getStatus());

    }

}
