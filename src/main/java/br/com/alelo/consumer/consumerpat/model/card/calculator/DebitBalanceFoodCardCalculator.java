package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

public class DebitBalanceFoodCardCalculator
    implements
        DebitBalanceCalculator
{
    private static final BigDecimal DISCOUNT_PERCENTAGE = new BigDecimal( "0.1" );

    @Override
    public BigDecimal calculateDiscount(
        final BigDecimal debitValue )
    {
        return debitValue.multiply( DISCOUNT_PERCENTAGE );
    }
}
