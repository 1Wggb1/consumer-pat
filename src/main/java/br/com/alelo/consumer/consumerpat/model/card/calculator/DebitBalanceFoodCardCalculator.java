package br.com.alelo.consumer.consumerpat.model.card.calculator;

public class DebitBalanceFoodCardCalculator
    implements
        DebitBalanceCalculator
{
    private static final Double DISCOUNT_PERCENTAGE = 0.1D;

    @Override
    public Long calculateDiscount(
        final Long debitValue )
    {
        return (long) ( debitValue * DISCOUNT_PERCENTAGE );
    }
}
