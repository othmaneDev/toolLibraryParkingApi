package com.tollParking.library.tollParkingLibrary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class tollParkingLibraryInitializationFailed extends Exception {
    public tollParkingLibraryInitializationFailed(String exceptionMessage) {
        super(exceptionMessage);
    }
}
