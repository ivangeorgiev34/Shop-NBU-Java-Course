package com.ivangeorgiev.shop.domain.utils;

import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;

public class Utils {
    public static void checkForNonPositiveNumber(double num) throws NegativeNumberException{
        if(num <= 0){
            throw new NegativeNumberException("Cannot enter non-positive number");
        }
    }

    public static void checkForNonPositiveNumber(int num) throws NegativeNumberException{
        if(num <= 0){
            throw new NegativeNumberException("Cannot enter non-positive number");
        }
    }
}
