package com.bytewheels.utils;


import java.util.Random;

/**
 * 
 * @author Lokesh
 */
public class RandomStringGenerator
{
    private static final char[] symbols = "123456789abcdedfghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ".toCharArray();
    private final Random random = new Random();
    private final char[] buffer;

    public RandomStringGenerator(int length)
    {
        if(length < 1)
        {
            throw new IllegalArgumentException("Password length cannot be less than 1" + length);
        }
        buffer = new char[length];
    }

    public String generateString()
    {
        for(int index = 0; index < buffer.length; ++index)
        {
            buffer[index] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buffer);
    }
}