package br.com.alelo.consumer.consumerpat.converter.card.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;
import br.com.alelo.consumer.consumerpat.converter.card.ConsumerCardConverter;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

@Component
public class ConsumerCardConverterImpl
    implements
        ConsumerCardConverter
{
    @Override
    public PersistentConsumerCard toModel(
        final Long generatedCardNumber,
        final ConsumerCardRequestDTO consumerCardRequestDTO,
        final PersistentConsumer persistentConsumer )
    {
        return PersistentConsumerCard.builder()
            .number( generatedCardNumber )
            .balance( Optional.ofNullable( consumerCardRequestDTO.balanceValue() ).orElse( BigDecimal.ZERO ) )
            .consumer( persistentConsumer )
            .establishmentType( CardEstablishmentType.getOrThrownException( consumerCardRequestDTO.cardEstablishmentType() ) )
            .build();
    }

    @Override
    public ConsumerCardResponseDTO toResponseDTO(
        final PersistentConsumerCard persistentConsumerCard )
    {
        return new ConsumerCardResponseDTO( persistentConsumerCard.getId(), persistentConsumerCard.getNumber() );
    }

    @Override
    public ConsumerCardDTO toDTO(
        final PersistentConsumerCard persistentConsumerCard )
    {
        return new ConsumerCardDTO( persistentConsumerCard.getId(),
            persistentConsumerCard.getNumber(),
            persistentConsumerCard.getBalance(),
            persistentConsumerCard.getEstablishmentType().name() );
    }
}