package br.com.alelo.consumer.consumerpat.converter.card;

import br.com.alelo.consumer.consumerpat.converter.PageableConverter;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

public interface ConsumerCardConverter
    extends
        PageableConverter<PersistentConsumerCard,ConsumerCardDTO>
{
    PersistentConsumerCard toModel(
        ConsumerCardRequestDTO consumerCardRequestDTO,
        PersistentConsumer persistentConsumer );

    ConsumerCardResponseDTO toResponseDTO(
        PersistentConsumerCard persistentConsumerCard );

    PersistentConsumerCard toModel(
        PersistentConsumerCard persistentConsumerCard,
        ConsumerCardUpdateRequestDTO cardUpdateRequestDTO );
}