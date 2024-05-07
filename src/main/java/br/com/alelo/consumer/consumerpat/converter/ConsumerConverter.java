package br.com.alelo.consumer.consumerpat.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import br.com.alelo.consumer.consumerpat.dto.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;

public interface ConsumerConverter
{
    PersistentConsumer toModel(
        ConsumerRequestDTO consumerRequestDTO );

    PersistentConsumer toModel(
        PersistentConsumer persistentConsumer,
        ConsumerRequestDTO consumerRequestDTO );

    ConsumerPageableDTO toPageableDTO(
        Page<PersistentConsumer> consumersPage );

    ConsumerDTO toDTO(
        PersistentConsumer persistentConsumer );

    default List<ConsumerDTO> toConsumerDTO(
        final List<PersistentConsumer> persistentConsumers )
    {
        return persistentConsumers.stream()
            .map( this::toDTO )
            .toList();
    }
}
