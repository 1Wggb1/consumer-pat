package br.com.alelo.consumer.consumerpat.model.card.calculator;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.alelo.consumer.consumerpat.DebitBalanceAssertTest;

class DebitBalanceDrugStoreCardCalculatorTest
    implements
        DebitBalanceAssertTest
{
    private final DebitBalanceDrugStoreCardCalculator subject = new DebitBalanceDrugStoreCardCalculator();

    @Test
    @DisplayName( "Deve retornar  0 como valor da taxa no débito de drogaria." )
    void shouldReturnZeroWhenDefaultTaxOnDrugstore()
    {
        assertEqualsCustom( BigDecimal.ZERO, subject.calculateTax( BigDecimal.valueOf( 10000L ) ) );
    }

    @Test
    @DisplayName( "Deve retornar 0 como valor do desconto no débito de drogaria." )
    void shouldReturnZeroWhenDefaultDiscountOnDrugstore()
    {
        assertEqualsCustom( BigDecimal.ZERO, subject.calculateDiscount( BigDecimal.valueOf( 10000L ) ) );
    }

    @Test
    @DisplayName( "Deve retornar mesmo valor do argumento quando calcular o total de débito de drogaria." )
    void shouldReturnSameArgumentValueAsTotal()
    {
        final BigDecimal debitValue = BigDecimal.valueOf( 10000L );
        assertEqualsCustom( debitValue, subject.calculateTotal( debitValue ) );
    }
}
