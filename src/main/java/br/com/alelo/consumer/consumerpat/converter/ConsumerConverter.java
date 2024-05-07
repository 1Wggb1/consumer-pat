package br.com.alelo.consumer.consumerpat.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;

public interface ConsumerConverter
{
    PersistentConsumer toModel(
        ConsumerRequestDTO consumerRequestDTO );

    PersistentConsumer toModel(
        PersistentConsumer persistentConsumer,
        ConsumerRequestDTO consumerRequestDTO );

    EntityPageableDTO<ConsumerDTO> toPageableDTO(
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
