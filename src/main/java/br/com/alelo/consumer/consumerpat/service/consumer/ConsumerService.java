package br.com.alelo.consumer.consumerpat.service.consumer;

import org.springframework.data.domain.Pageable;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerResponseDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

public interface ConsumerService
{
    ConsumerResponseDTO create(
        ConsumerRequestDTO consumerRequestDTO );

    EntityPageableDTO<ConsumerDTO> findConsumers(
        Pageable pageable );

    ConsumerDTO findConsumerById(
        Integer id );

    void update(
        Integer id,
        ConsumerRequestDTO consumerRequestDTO );

    PersistentConsumer findByIdOrThrowException(
        Integer id );
}
