package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

public class DebitBalanceFuelCardCalculator
    implements
        DebitBalanceCalculator
{
    private static final BigDecimal TAX_PERCENTAGE = new BigDecimal( "0.35" );

    @Override
    public BigDecimal calculateTax(
        final BigDecimal debitValue )
    {
        return debitValue.multiply( TAX_PERCENTAGE );
    }
}