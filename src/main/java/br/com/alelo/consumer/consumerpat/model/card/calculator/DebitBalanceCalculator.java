package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

public interface DebitBalanceCalculator
{
    default BigDecimal calculateDiscount(
        final BigDecimal debitValue )
    {
        return BigDecimal.ZERO;
    }

    default BigDecimal calculateTax(
        final BigDecimal debitValue )
    {
        return BigDecimal.ZERO;
    }

    default BigDecimal calculateTotal(
        final BigDecimal debitValue )
    {
        final BigDecimal discount = debitValue.subtract( calculateDiscount( debitValue ) );
        return discount.add( calculateTax( debitValue ) );
    }
}