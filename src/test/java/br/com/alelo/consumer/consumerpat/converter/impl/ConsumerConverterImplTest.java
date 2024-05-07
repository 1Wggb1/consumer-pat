package br.com.alelo.consumer.consumerpat.converter.impl;

import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_ADDRESS;
import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_CONTACT;
import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_DTO;
import static br.com.alelo.consumer.consumerpat.TestData.VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.consumer.ConsumerContact;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

class ConsumerConverterImplTest
{

    private final ConsumerConverterImpl subject = new ConsumerConverterImpl();

    @Test
    @DisplayName( "Deve retornar persistent a partir de DTO." )
    void shouldReturnPersistentFromDTO()
    {
        final PersistentConsumer persistentConsumer = subject.toModel( CONSUMER_DTO );

        assertDTOAndPersistentFields( CONSUMER_DTO, persistentConsumer );
    }

    private static void assertDTOAndPersistentFields(
        final ConsumerRequestDTO expectedConsumer,
        final PersistentConsumer persistentConsumer )
    {
        assertEquals( expectedConsumer.name(), persistentConsumer.getName() );
        assertEquals( LocalDate.parse( expectedConsumer.birthday(), DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ),
            persistentConsumer.getBirthday() );
        assertEquals( expectedConsumer.documentNumber().replaceAll( "[-.]", "" ),
            persistentConsumer.getDocumentNumber() );
        assertEquals( expectedConsumer.address(), persistentConsumer.getAddress() );
        assertEquals( expectedConsumer.contact(), persistentConsumer.getContact() );
    }

    @ParameterizedTest
    @NullSource
    @DisplayName( "Deve retornar nulo quando data nula." )
    void shouldReturnNullWhenDateNull(
        final String nullDate )
    {
        assertNull( ConsumerConverterImpl.convertToLocalDate( nullDate ) );
    }

    @Test
    @DisplayName( "Deve retornar data." )
    void shouldReturnDateAsISO()
    {
        assertEquals( LocalDate.of( 1999, 4, 17 ), ConsumerConverterImpl.convertToLocalDate( "1999-04-17" ) );
    }

    @Test
    @DisplayName( "Deve atualizar campos do persistent a partir do DTO." )
    void shouldUpdatePersistentWithDTO()
    {
        final ConsumerContact consumerContact = new ConsumerContact( 785544488L,
            1234568L,
            "aae@protomail.ru" );
        final ConsumerAddress consumerAddress = new ConsumerAddress( "Rua Viriginia", "211",
            "Taboão", "BRA", "06766142" );
        final ConsumerRequestDTO consumerDTO = new ConsumerRequestDTO( "Marcos",
            "690.149.110-76",
            "1999-04-17",
            consumerContact,
            consumerAddress );
        final PersistentConsumer persistentConsumer = createDefaultPersistent( 1 ).build();

        final PersistentConsumer updated = subject.toModel( persistentConsumer, consumerDTO );

        assertDTOAndPersistentFields( consumerDTO, updated );
    }

    private static PersistentConsumer.PersistentConsumerBuilder createDefaultPersistent(
        final Integer id )
    {
        return PersistentConsumer.builder()
            .id( id )
            .name( "Will" )
            .documentNumber( VALID_DOCUMENT_NUMBER_WITHOUT_MASK )
            .birthday( LocalDate.of( 1888, 12, 27 ) )
            .contact( CONSUMER_CONTACT )
            .address( CONSUMER_ADDRESS );
    }

    @Test
    @DisplayName( "Deve retornar pageable com entidades." )
    void shouldReturnPageableFromPersistentPage()
    {
        final PageRequest pageRequest = PageRequest.of( 0, 2 );
        final int totalElements = 5;
        final Page<PersistentConsumer> persistentConsumerPage = new PageImpl<>(
            List.of( createDefaultPersistent( 1 ).build(),
                createDefaultPersistent( 2 ).build() ),
            pageRequest, totalElements );

        final EntityPageableDTO<ConsumerDTO> entityPageableDTO = subject.toPageableDTO( persistentConsumerPage );

        final PageableDTO pageable = entityPageableDTO.pageable();
        assertEquals( pageRequest.getPageNumber(), pageable.page() );
        assertEquals( pageRequest.getPageSize(), pageable.size() );
        assertEquals( 2, pageable.numberOfElements() );
        assertEquals( 3, pageable.totalPages() );
        assertEquals( totalElements, pageable.totalElements() );
        assertEquals( 2, entityPageableDTO.elements().size() );
    }
}