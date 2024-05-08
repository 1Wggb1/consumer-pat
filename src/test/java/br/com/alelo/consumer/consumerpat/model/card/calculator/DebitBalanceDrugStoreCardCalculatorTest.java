package br.com.alelo.consumer.consumerpat.model.card.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DebitBalanceDrugStoreCardCalculatorTest
{
    private final DebitBalanceDrugStoreCardCalculator subject = new DebitBalanceDrugStoreCardCalculator();

    @Test
    @DisplayName( "Deve retornar  0 como valor da taxa no débito de drogaria." )
    void shouldReturnZeroWhenDefaultTaxOnDrugstore()
    {
        assertEquals( 0L, subject.calculateTax( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar 0 como valor do desconto no débito de drogaria." )
    void shouldReturnZeroWhenDefaultDiscountOnDrugstore()
    {
        assertEquals( 0L, subject.calculateDiscount( 10000L ) );
    }

    @Test
    @DisplayName( "Deve retornar mesmo valor do argumento quando calcular o total de débito de drogaria." )
    void shouldReturnSameArgumentValueAsTotal()
    {
        final long debitValue = 10000L;
        assertEquals( debitValue, subject.calculateTotal( debitValue ) );
    }
}
