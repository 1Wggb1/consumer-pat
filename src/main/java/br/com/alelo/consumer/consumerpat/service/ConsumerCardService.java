package br.com.alelo.consumer.consumerpat.service;

import org.springframework.data.domain.Pageable;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardCreditBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;

public interface ConsumerCardService
{
    ConsumerCardResponseDTO createCard(
        Integer consumerId,
        ConsumerCardRequestDTO consumerCardDTO );

    void updateCard(
        Integer consumerId,
        Integer cardId,
        ConsumerCardUpdateRequestDTO consumerCardDTO );

    EntityPageableDTO<ConsumerCardDTO> findConsumersCards(
        Integer consumerId,
        Pageable pageable );

    void creditCardBalance(
        Integer consumerId,
        Integer cardId,
        CardCreditBalanceRequestDTO creditBalanceRequestDTO );

    CardDebitBalanceResponseDTO debitCardBalance(
        Integer consumerId,
        Integer cardId,
        CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO );
}
