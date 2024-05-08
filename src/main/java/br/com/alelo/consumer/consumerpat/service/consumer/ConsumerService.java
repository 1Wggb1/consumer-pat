package br.com.alelo.consumer.consumerpat.service.consumer;

import org.springframework.data.domain.Pageable;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerResponseDTO;

public interface ConsumerService
{
    ConsumerResponseDTO create(
        ConsumerRequestDTO consumerRequestDTO );

    EntityPageableDTO<ConsumerDTO> findConsumers(
        Pageable pageable );

    void update(
        Integer id,
        ConsumerRequestDTO consumerRequestDTO );
}
