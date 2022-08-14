package com.udacity.vehicles.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CarNotFoundException
 * This creates a CarNotFoundException that can be thrown when an issue arises in the CarService.
 *
 */
@ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "Car not found")
public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException() {
    }

    public CarNotFoundException(String message) {
        super(message);
    }
}
