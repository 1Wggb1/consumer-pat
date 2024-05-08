package br.com.alelo.consumer.consumerpat.model.card.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DebitBalanceFuelCardCalculatorTest
{
    private final DebitBalanceFuelCardCalculator subject = new DebitBalanceFuelCardCalculator();

    @Test
    @DisplayName( "Deve retornar 35% do valor como taxa do débito para combustível." )
    void shouldReturnZeroWhenDefaultTaxOnFood()
    {
        assertEquals( 3500L, subject.calculateTax( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar 0 como valor de desconto para combustível." )
    void shouldReturnZeroWhenDefaultDiscountOnFood()
    {
        assertEquals( 0L, subject.calculateDiscount( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar valor com 35% de taxa quando calcular o total de débito para combustível." )
    void shouldReturn10PercentDiscountArgumentValueAsTotalOnFood()
    {
        final long debitValue = 10000L;
        assertEquals( debitValue + 3500L, subject.calculateTotal( debitValue ) );
    }
}