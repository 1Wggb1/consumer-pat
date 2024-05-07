package br.com.alelo.consumer.consumerpat.converter.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import br.com.alelo.consumer.consumerpat.converter.ConsumerConverter;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.util.UnmaskUtil;

@Component
public class ConsumerConverterImpl
    implements
        ConsumerConverter
{
    @Override
    public PersistentConsumer toModel(
        final ConsumerRequestDTO consumerRequestDTO )
    {
        return PersistentConsumer.builder()
            .name( consumerRequestDTO.name() )
            .documentNumber( UnmaskUtil.unmaskDocumentNumber( consumerRequestDTO.documentNumber() ) )
            .birthday( convertToLocalDate( consumerRequestDTO.birthday() ) )
            .contact( consumerRequestDTO.contact() )
            .address( consumerRequestDTO.address() )
            .build();
    }

    // Visible for test
    static LocalDate convertToLocalDate(
        final String date )
    {
        final String unmaskedDate = UnmaskUtil.unmaskDate( date );
        if( unmaskedDate == null ) {
            return null;
        }
        return LocalDate.parse( unmaskedDate, DateTimeFormatter.BASIC_ISO_DATE );
    }

    @Override
    public ConsumerDTO toDTO(
        final PersistentConsumer persistentConsumer )
    {
        return new ConsumerDTO( persistentConsumer.getId(),
            persistentConsumer.getName(),
            persistentConsumer.getDocumentNumber(),
            persistentConsumer.getBirthday(),
            persistentConsumer.getContact(),
            persistentConsumer.getAddress() );
    }

    @Override
    public PersistentConsumer toModel(
        final PersistentConsumer persistentConsumer,
        final ConsumerRequestDTO consumerRequestDTO )
    {
        persistentConsumer.setName( consumerRequestDTO.name() );
        persistentConsumer.setBirthday( convertToLocalDate( consumerRequestDTO.birthday() ) );
        persistentConsumer.setDocumentNumber( consumerRequestDTO.documentNumber() );
        persistentConsumer.setContact( consumerRequestDTO.contact() );
        persistentConsumer.setAddress( consumerRequestDTO.address() );
        return persistentConsumer;
    }
}
