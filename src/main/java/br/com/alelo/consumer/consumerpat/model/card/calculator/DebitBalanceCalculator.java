package br.com.alelo.consumer.consumerpat.model.card.calculator;

public interface DebitBalanceCalculator
{
    default Long calculateDiscount(
        final Long value )
    {
        return 0L;
    }

    default Long calculateTax(
        final Long value )
    {
        return 0L;
    }

    default Long calculateTotal(
        final Long value )
    {
        return value - calculateDiscount( value ) + calculateTax( value );
    }
}