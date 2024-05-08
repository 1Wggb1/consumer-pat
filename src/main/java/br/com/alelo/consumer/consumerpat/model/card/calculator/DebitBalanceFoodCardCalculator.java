package br.com.alelo.consumer.consumerpat.model.card.calculator;

public class DebitBalanceFoodCardCalculator
    implements
        DebitBalanceCalculator
{
    private static final Double DISCOUNT_PERCENTAGE = 0.1D;

    @Override
    public Long calculateDiscount(
        final Long value )
    {
        return (long) ( value * DISCOUNT_PERCENTAGE );
    }
}
