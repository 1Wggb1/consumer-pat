package br.com.alelo.consumer.consumerpat.model.card;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInsufficientBalanceException;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInvalidCreditValueException;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

class PersistentConsumerCardTest
{
    @ParameterizedTest
    @MethodSource( "invalidCreditValue" )
    @DisplayName( "Deve lançar exceção quando crétido é nulo ou vazio." )
    void shouldThrowExceptionWhenCreditIsNullOrEmpty(
        final Long invalidCreditValue )
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .balanceCents( 0L )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();

        assertThrows( ConsumerCardInvalidCreditValueException.class, () -> consumerCard.addCredit( invalidCreditValue ) );
        assertThrows( ConsumerCardInvalidCreditValueException.class, () -> consumerCard.setBalanceCents( invalidCreditValue ) );
    }

    private static Stream<Long> invalidCreditValue()
    {
        return Stream.of( null, - 15L );
    }

    @Test
    @DisplayName( "Deve lançar exceção quando débito maior que saldo." )
    void shouldThrownExceptionWhenDebitGreaterThanBalance()
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .balanceCents( 10L )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();

        assertThrows( ConsumerCardInsufficientBalanceException.class, () -> consumerCard.debit( 11L ) );
    }

    @ParameterizedTest
    @ValueSource( longs = {
        9, 10, 1
    } )
    @DisplayName( "Deve lançar exceção quando débito maior que saldo." )
    void shouldNotThrownExceptionWhenDebitLessThanEqualsBalance(
        final Long debitValue )
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .number( 122255L )
            .balanceCents( 10L )
            .establishmentType( CardEstablishmentType.FUEL )
            .consumer( PersistentConsumer.builder().id( 1 ).build() )
            .build();
        final Long balanceCentsBeforeDebit = consumerCard.getBalanceCents();

        assertDoesNotThrow( () -> consumerCard.debit( debitValue ) );
        assertEquals( balanceCentsBeforeDebit - debitValue, consumerCard.getBalanceCents() );
    }
}