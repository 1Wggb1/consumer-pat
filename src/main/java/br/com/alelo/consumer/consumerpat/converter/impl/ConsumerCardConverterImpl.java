package br.com.alelo.consumer.consumerpat.converter.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;
import br.com.alelo.consumer.consumerpat.converter.ConsumerCardConverter;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
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
        final ConsumerCardRequestDTO consumerCardRequestDTO,
        final PersistentConsumer persistentConsumer )
    {
        return PersistentConsumerCard.builder()
            .number( consumerCardRequestDTO.number() )
            .balanceCents( Optional.ofNullable( consumerCardRequestDTO.balanceValueCents() ).orElse( 0L ) )
            .consumer( persistentConsumer )
            .establishmentType( CardEstablishmentType.getOrThrownException( consumerCardRequestDTO.cardEstablishmentType() ) )
            .build();
    }

    @Override
    public ConsumerCardResponseDTO toResponseDTO(
        final PersistentConsumerCard persistentConsumerCard )
    {
        return new ConsumerCardResponseDTO( persistentConsumerCard.getId() );
    }

    @Override
    public PersistentConsumerCard toModel(
        final PersistentConsumerCard persistentConsumerCard,
        final ConsumerCardUpdateRequestDTO cardUpdateRequestDTO )
    {
        persistentConsumerCard.setNumber( cardUpdateRequestDTO.number() );
        persistentConsumerCard.setEstablishmentType(
            CardEstablishmentType.getOrThrownException( cardUpdateRequestDTO.cardEstablishmentType() ) );
        return persistentConsumerCard;
    }

    @Override
    public ConsumerCardDTO toDTO(
        final PersistentConsumerCard persistentConsumerCard )
    {
        return new ConsumerCardDTO( persistentConsumerCard.getId(),
            persistentConsumerCard.getNumber(),
            persistentConsumerCard.getBalanceCents(),
            persistentConsumerCard.getEstablishmentType().name() );
    }
}
