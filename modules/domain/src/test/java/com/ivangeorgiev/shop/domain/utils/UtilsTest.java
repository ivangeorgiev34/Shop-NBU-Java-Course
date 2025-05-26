package com.ivangeorgiev.shop.domain.utils;

import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest {

    @Test
    public void checkForNonPositiveNumber_shouldThrowException() {
        assertThrows(NegativeNumberException.class,() -> Utils.checkForNonPositiveNumber(-1),"Cannot enter non-positive number");
    }
}
