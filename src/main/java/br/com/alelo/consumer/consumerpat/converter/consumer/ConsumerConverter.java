package br.com.alelo.consumer.consumerpat.converter.consumer;

import br.com.alelo.consumer.consumerpat.converter.PageableConverter;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

public interface ConsumerConverter
    extends
        PageableConverter<PersistentConsumer,ConsumerDTO>
{
    PersistentConsumer toModel(
        ConsumerRequestDTO consumerRequestDTO );

    PersistentConsumer toModel(
        PersistentConsumer persistentConsumer,
        ConsumerRequestDTO consumerRequestDTO );
}
