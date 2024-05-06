package br.com.alelo.consumer.consumerpat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import br.com.alelo.consumer.consumerpat.entity.Consumer;

public interface ConsumerService
{
    Consumer create(
        Consumer consumer );

    List<Consumer> findConsumers(
        Pageable pageable );

    Consumer update(
        Integer id,
        Consumer consumer );

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
