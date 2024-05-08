package br.com.alelo.consumer.consumerpat.model.card.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DebitBalanceFoodCardCalculatorTest
{
    private final DebitBalanceFoodCardCalculator subject = new DebitBalanceFoodCardCalculator();

    @Test
    @DisplayName( "Deve retornar 0 como valor da taxa do débito para alimentação." )
    void shouldReturnZeroWhenDefaultTaxOnFood()
    {
        assertEquals( 0L, subject.calculateTax( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar 10% do valor como desconto no débito para alimentação." )
    void shouldReturnZeroWhenDefaultDiscountOnFood()
    {
        assertEquals( 1000L, subject.calculateDiscount( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar valor com 10% de desconto quando calcular o total de débito para alimentação." )
    void shouldReturn10PercentDiscountArgumentValueAsTotalOnFood()
    {
        assertEquals( 9000L, subject.calculateTotal( 10000L ) );
    }
}
