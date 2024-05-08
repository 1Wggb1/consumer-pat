package br.com.alelo.consumer.consumerpat.util;

import java.util.Random;

public final class CardNumberGeneratorUtil
{
    private static final Long MIN_NUMBER = 100_000_000_000L;
    private static final Long CARD_NUMBER_SIZE = 900_000_000_000L;
    private static final Random RANDOM = new Random();

    public static Long generateCardNumber()
    {
        return MIN_NUMBER + RANDOM.nextLong( CARD_NUMBER_SIZE );
    }
}
