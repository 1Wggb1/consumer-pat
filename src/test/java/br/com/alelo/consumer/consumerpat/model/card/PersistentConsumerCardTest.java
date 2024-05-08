package br.com.alelo.consumer.consumerpat.model.card;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import br.com.alelo.consumer.consumerpat.DebitBalanceAssertTest;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInsufficientBalanceException;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInvalidCreditValueException;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

class PersistentConsumerCardTest
    implements
        DebitBalanceAssertTest
{
    @ParameterizedTest
    @MethodSource( "invalidCreditValue" )
    @DisplayName( "Deve lançar exceção quando crétido é nulo ou vazio." )
    void shouldThrowExceptionWhenCreditIsNullOrEmpty(
        final BigDecimal invalidCreditValue )
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();

        assertThrows( ConsumerCardInvalidCreditValueException.class, () -> consumerCard.addCredit( invalidCreditValue ) );
        assertThrows( ConsumerCardInvalidCreditValueException.class, () -> consumerCard.setBalance( invalidCreditValue ) );
    }

    private static Stream<BigDecimal> invalidCreditValue()
    {
        return Stream.of( null, new BigDecimal( - 1 ) );
    }

    @Test
    @DisplayName( "Deve lançar exceção quando débito maior que saldo." )
    void shouldThrownExceptionWhenDebitGreaterThanBalance()
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .balance( BigDecimal.valueOf( 10L ) )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();

        assertThrows( ConsumerCardInsufficientBalanceException.class, () -> consumerCard.debit( BigDecimal.valueOf( 11L ) ) );
    }

    @ParameterizedTest
    @MethodSource( "validValues" )
    @DisplayName( "Deve lançar exceção quando débito maior que saldo." )
    void shouldNotThrownExceptionWhenDebitLessThanEqualsBalance(
        final BigDecimal debitValue )
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .balance( new BigDecimal( 10L ) )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();

        final BigDecimal balanceBeforeDebit = consumerCard.getBalance();

        assertDoesNotThrow( () -> consumerCard.debit( debitValue ) );
        assertEqualsCustom( balanceBeforeDebit.subtract( debitValue ), consumerCard.getBalance() );
    }

    private static Stream<BigDecimal> validValues()
    {
        return Stream.of( new BigDecimal( 9 ), new BigDecimal( 10 ), BigDecimal.ONE );
    }
}