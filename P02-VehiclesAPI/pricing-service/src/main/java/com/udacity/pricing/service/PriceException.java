package com.udacity.pricing.service;

/**
 *
 * PriceException
 * This creates a PriceException that can be thrown when an issue arises in the PricingService.
 *
 */
public class PriceException extends Exception {

    public PriceException(String message) {
        super(message);
    }
}
