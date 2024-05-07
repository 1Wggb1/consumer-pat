package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;

public interface ConsumerCardService
{
    CardDebitBalanceResponseDTO createCard(
        Integer consumerId,
        ConsumerCardRequestDTO consumerCardDTO );

    void updateCard(
        Integer consumerId,
        ConsumerCardRequestDTO consumerCardDTO );

    EntityPageableDTO<ConsumerCardRequestDTO> findConsumersCards(
        Integer consumerId );

    void creditCardBalance(
        Integer consumerId,
        Integer cardId,
        Long creditValue );

    CardDebitBalanceResponseDTO debitCardBalance(
        Integer consumerId,
        Integer cardId,
        CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO );
}
