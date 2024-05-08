package br.com.alelo.consumer.consumerpat.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardNumberGeneratorUtilTest
{
    @Test
    void shouldGeneratorNumberWithSize12()
    {
        Assertions.assertEquals( 12, CardNumberGeneratorUtil.generateCardNumber().toString().length() );
    }
}
