package br.com.alelo.consumer.consumerpat.model.card.calculator;

public interface DebitBalanceCalculator
{
    default Long calculateDiscount(
        final Long debitValue )
    {
        return 0L;
    }

    default Long calculateTax(
        final Long debitValue )
    {
        return 0L;
    }

    default Long calculateTotal(
        final Long debitValue )
    {
        return debitValue - calculateDiscount( debitValue ) + calculateTax( debitValue );
    }
}