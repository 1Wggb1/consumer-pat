package br.com.alelo.consumer.consumerpat.converter.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import br.com.alelo.consumer.consumerpat.converter.ConsumerConverter;
import br.com.alelo.consumer.consumerpat.dto.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;
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
    public ConsumerPageableDTO toPageableDTO(
        final Page<PersistentConsumer> consumersPage )
    {
        final PageableDTO pageableDTO = new PageableDTO(
            consumersPage.getNumber(),
            consumersPage.getSize(),
            consumersPage.getNumberOfElements(),
            consumersPage.getTotalPages(),
            (int) consumersPage.getTotalElements() );
        return new ConsumerPageableDTO( pageableDTO, toConsumerDTO( consumersPage.getContent() ) );
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
