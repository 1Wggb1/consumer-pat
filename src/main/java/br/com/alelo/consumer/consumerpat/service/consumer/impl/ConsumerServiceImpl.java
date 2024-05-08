package br.com.alelo.consumer.consumerpat.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.alelo.consumer.consumerpat.converter.ConsumerConverter;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerResponseDTO;
import br.com.alelo.consumer.consumerpat.exception.consumer.ConsumerNotFoundException;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ConsumerServiceImpl
    implements
        ConsumerService
{
    @Autowired
    private ConsumerRepository repository;
    @Autowired
    private ConsumerConverter converter;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;

    @Override
    public ConsumerResponseDTO create(
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        final String documentNumber = consumerRequestDTO.documentNumber();
        logWithTrace( traceId, String.format( "Creating consumer with documentNumber = %s...", documentNumber ) );
        final PersistentConsumer saved = repository.save( converter.toModel( consumerRequestDTO ) );
        logWithTrace( traceId, String.format( "Consumer with  id = %d and  documentNumber = %s created successfully!",
            saved.getId(), documentNumber ) );
        return new ConsumerResponseDTO( saved.getId() );
    }

    private static void logWithTrace(
        final UUID traceId,
        final String logMessage )
    {
        log.info( "TRACEID = {} - {}", traceId, logMessage );
    }

    @Override
    public EntityPageableDTO<ConsumerDTO> findConsumers(
        final Pageable pageable )
    {
        final UUID traceId = UUID.randomUUID();
        logWithTrace( traceId, String.format( "Finding consumers by %s", pageable ) );
        final Page<PersistentConsumer> consumersPage = repository.findAll( pageable );
        logWithTrace( traceId, String.format( "Consumers found by %s", pageable ) );
        return converter.toPageableDTO( consumersPage );
    }

    @Override
    public void update(
        final Integer id,
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        logWithTrace( traceId, String.format( "Updating consumer by id = %d", id ) );
        final PersistentConsumer persistentConsumer = repository.findById( id )
            .orElseThrow( () -> new ConsumerNotFoundException( id ) );
        final PersistentConsumer updatedConsumer = converter.toModel( persistentConsumer, consumerRequestDTO );
        repository.save( updatedConsumer );
        logWithTrace( traceId, String.format( "Consumer with id = %d updated successfully!", id ) );
    }
}
