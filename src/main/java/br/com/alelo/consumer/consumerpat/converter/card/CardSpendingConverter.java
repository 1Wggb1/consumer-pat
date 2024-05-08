package br.com.alelo.consumer.consumerpat.converter.card;

import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;

public interface CardSpendingConverter
{
    PersistentCardSpending toModel(
        Long totalDebit,
        PersistentConsumerCard consumerCard,
        CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO );

    CardDebitBalanceResponseDTO toDTO(
        PersistentCardSpending cardSpending );
}