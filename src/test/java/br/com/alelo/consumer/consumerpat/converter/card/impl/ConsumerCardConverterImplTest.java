package br.com.alelo.consumer.consumerpat.converter.card.impl;

import static br.com.alelo.consumer.consumerpat.TestData.VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardEstablishmentTypeNotFoundException;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

class ConsumerCardConverterImplTest
{
    private static final PersistentConsumer DEFAULT_CONSUMER = PersistentConsumer.builder()
        .id( 1 )
        .name( "João" )
        .documentNumber( VALID_DOCUMENT_NUMBER_WITHOUT_MASK )
        .build();

    private final ConsumerCardConverterImpl subject = new ConsumerCardConverterImpl();

    @Test
    @DisplayName( "Deve retornar persistent a partir do DTO (ConsumerCard)." )
    void shouldReturnPersistentFromDTO()
    {
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO(
            BigDecimal.valueOf( 1000L ),
            CardEstablishmentType.FUEL.name() );
        final PersistentConsumer consumer = PersistentConsumer.builder()
            .id( 1 )
            .name( "João" )
            .documentNumber( VALID_DOCUMENT_NUMBER_WITHOUT_MASK )
            .build();
        final long generatedCardNumber = 88888L;

        final PersistentConsumerCard persistentConsumerCard = subject.toModel( generatedCardNumber, cardRequestDTO, consumer );

        assertEquals( cardRequestDTO.balanceValue(), persistentConsumerCard.getBalance() );
        assertEquals( cardRequestDTO.cardEstablishmentType(), persistentConsumerCard.getEstablishmentType().name() );
        assertEquals( generatedCardNumber, persistentConsumerCard.getNumber() );
        assertEquals( persistentConsumerCard.getConsumer(), consumer );
    }

    @ParameterizedTest
    @ValueSource( strings = {
        "inalid", "drug", "fule"
    } )
    @DisplayName( "Deve retornar persistent a partir do DTO (ConsumerCard)." )
    void shouldThrowExceptionWhenCardEstablishmentTypeInvalidOnConversion(
        final String cardEstablishmentType )
    {
        final ConsumerCardRequestDTO cardRequestDTO = new ConsumerCardRequestDTO( BigDecimal.valueOf( 1000L ),
            cardEstablishmentType );
        final PersistentConsumer consumer = PersistentConsumer.builder()
            .id( 1 )
            .name( "João" )
            .documentNumber( VALID_DOCUMENT_NUMBER_WITHOUT_MASK )
            .build();

        assertThrows( ConsumerCardEstablishmentTypeNotFoundException.class, () -> subject.toModel( 8888888L, cardRequestDTO, consumer ) );
    }

    @Test
    @DisplayName( "Deve retornar DTO a partir de persitent." )
    void shouldCreateDTOFromPersistent()
    {
        final PersistentConsumerCard persistentConsumerCard = createDefaultConsumerCard( 1,
            CardEstablishmentType.DRUGSTORE ).build();

        final ConsumerCardDTO consumerCardDTO = subject.toDTO( persistentConsumerCard );

        assertEquals( persistentConsumerCard.getId(), consumerCardDTO.id() );
        assertEquals( persistentConsumerCard.getNumber(), consumerCardDTO.number() );
        assertEquals( persistentConsumerCard.getBalance(), consumerCardDTO.balanceValueCents() );
        assertEquals( persistentConsumerCard.getEstablishmentType().name(), consumerCardDTO.cardEstablishmentType() );
    }

    private static PersistentConsumerCard.PersistentConsumerCardBuilder createDefaultConsumerCard(
        final Integer id,
        final CardEstablishmentType establishmentType )
    {
        return PersistentConsumerCard.builder()
            .id( id )
            .establishmentType( establishmentType )
            .consumer( DEFAULT_CONSUMER )
            .balance( BigDecimal.valueOf( 1000L ) )
            .number( 154785966555L );
    }

    @Test
    @DisplayName( "Deve retornar pageable com entidades." )
    void shouldReturnPageableFromPersistentPage()
    {
        final PageRequest pageRequest = PageRequest.of( 0, 2 );
        final int totalElements = 5;
        final Page<PersistentConsumerCard> persistentConsumerPage = new PageImpl<>(
            List.of( createDefaultConsumerCard( 1, CardEstablishmentType.FOOD ).build(),
                createDefaultConsumerCard( 2, CardEstablishmentType.FUEL ).build() ),
            pageRequest, totalElements );

        final EntityPageableDTO<ConsumerCardDTO> entityPageableDTO = subject.toPageableDTO( persistentConsumerPage );

        final PageableDTO pageable = entityPageableDTO.pageable();
        assertEquals( pageRequest.getPageNumber(), pageable.page() );
        assertEquals( pageRequest.getPageSize(), pageable.size() );
        assertEquals( 2, pageable.numberOfElements() );
        assertEquals( 3, pageable.totalPages() );
        assertEquals( totalElements, pageable.totalElements() );
        assertEquals( 2, entityPageableDTO.elements().size() );
    }
}