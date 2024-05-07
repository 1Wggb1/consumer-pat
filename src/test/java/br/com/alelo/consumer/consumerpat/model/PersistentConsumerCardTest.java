package br.com.alelo.consumer.consumerpat.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInvalidCreditValueException;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

class PersistentConsumerCardTest
{
    @ParameterizedTest
    @MethodSource( "invalidCreditValue" )
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
}
