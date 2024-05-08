package br.com.alelo.consumer.consumerpat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

public interface DebitBalanceAssertTest
{
    default void assertEqualsCustom(
        final BigDecimal expected,
        final BigDecimal result )
    {
        Assertions.assertEquals( 0, expected.compareTo( result ) );
    }
}
