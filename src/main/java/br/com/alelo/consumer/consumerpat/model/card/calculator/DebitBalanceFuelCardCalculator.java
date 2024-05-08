package br.com.alelo.consumer.consumerpat.model.card.calculator;

public class DebitBalanceFuelCardCalculator
    implements
        DebitBalanceCalculator
{
    private static final Double TAX_PERCENTAGE = 0.35D;

    @Override
    public Long calculateTax(
        final Long value )
    {
        return (long) ( value * TAX_PERCENTAGE );
    }
}