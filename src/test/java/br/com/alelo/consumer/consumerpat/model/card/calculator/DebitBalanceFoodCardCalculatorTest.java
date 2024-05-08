package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.alelo.consumer.consumerpat.DebitBalanceAssertTest;

class DebitBalanceFoodCardCalculatorTest
    implements
        DebitBalanceAssertTest
{
    private final DebitBalanceFoodCardCalculator subject = new DebitBalanceFoodCardCalculator();

    @Test
    @DisplayName( "Deve retornar 0 como valor da taxa do débito para alimentação." )
    void shouldReturnZeroWhenDefaultTaxOnFood()
    {
        assertEqualsCustom( BigDecimal.ZERO, subject.calculateTax( new BigDecimal( 10000L ) ) );
    }

    @Test
    @DisplayName( "Deve retornar 10% do valor como desconto no débito para alimentação." )
    void shouldReturnZeroWhenDefaultDiscountOnFood()
    {
        assertEqualsCustom( new BigDecimal( 1000L ), subject.calculateDiscount( BigDecimal.valueOf( 10000L ) ) );
    }

    @Test
    @DisplayName( "Deve retornar valor com 10% de desconto quando calcular o total de débito para alimentação." )
    void shouldReturn10PercentDiscountArgumentValueAsTotalOnFood()
    {
        assertEqualsCustom( new BigDecimal( 9000L ), subject.calculateTotal( BigDecimal.valueOf( 10000L ) ) );
    }
}
