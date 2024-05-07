package br.com.alelo.consumer.consumerpat.service;

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

    void setBalance(
        Integer cardNumber,
        Double value );

    void buy(
        Integer establishmentType,
        String establishmentName,
        Integer cardNumber,
        String productDescription,
        Double value );
}
