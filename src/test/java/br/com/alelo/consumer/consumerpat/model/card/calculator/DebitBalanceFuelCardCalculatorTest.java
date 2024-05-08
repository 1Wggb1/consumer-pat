package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.alelo.consumer.consumerpat.DebitBalanceAssertTest;

class DebitBalanceFuelCardCalculatorTest
    implements
        DebitBalanceAssertTest
{
    private final DebitBalanceFuelCardCalculator subject = new DebitBalanceFuelCardCalculator();

    @Test
    @DisplayName( "Deve retornar 35% do valor como taxa do débito para combustível." )
    void shouldReturnZeroWhenDefaultTaxOnFood()
    {
        final BigDecimal result = subject.calculateTax( new BigDecimal( 10000L ) );

        assertEqualsCustom( new BigDecimal( 3500L ), result );
    }

    @Test
    @DisplayName( "Deve retornar 0 como valor de desconto para combustível." )
    void shouldReturnZeroWhenDefaultDiscountOnFood()
    {
        assertEqualsCustom( BigDecimal.ZERO, subject.calculateDiscount( BigDecimal.valueOf( 10000L ) ) );
    }

    @Test
    @DisplayName( "Deve retornar valor com 35% de taxa quando calcular o total de débito para combustível." )
    void shouldReturn10PercentDiscountArgumentValueAsTotalOnFood()
    {
        final BigDecimal debitValue = BigDecimal.valueOf( 10000L );

        assertEqualsCustom( debitValue.add( new BigDecimal( 3500L ) ), subject.calculateTotal( debitValue ) );
    }
}