package br.com.alelo.consumer.consumerpat.service;

import org.springframework.data.domain.Pageable;
import br.com.alelo.consumer.consumerpat.dto.ConsumerPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerResponseDTO;

public interface ConsumerService
{
    ConsumerResponseDTO create(
        ConsumerRequestDTO consumerRequestDTO );

    ConsumerPageableDTO findConsumers(
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
