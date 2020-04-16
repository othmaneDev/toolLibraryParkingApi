package com.tollParking.library.tollParkingLibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidPricingPolicy extends Exception {
    public InvalidPricingPolicy(String exceptionMessage) {
        super(exceptionMessage);
    }
}
