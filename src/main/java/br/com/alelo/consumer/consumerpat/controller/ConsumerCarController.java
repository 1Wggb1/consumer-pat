package br.com.alelo.consumer.consumerpat.controller;

import java.net.URI;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.service.ConsumerCardService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping( "/v1/consumers/{consumer_id}/cards" )
public class ConsumerCarController
{
    private ConsumerCardService service;

    @PostMapping
    public ResponseEntity<CardDebitBalanceResponseDTO> createCard(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @Valid @RequestBody final ConsumerCardRequestDTO consumerCardDTO )
    {
        final CardDebitBalanceResponseDTO responseDTO = service.createCard( consumerId, consumerCardDTO );
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .buildAndExpand( responseDTO.transactionId() )
            .toUri();
        return ResponseEntity.created( location ).body( responseDTO );
    }

    @PutMapping
    public ResponseEntity<Void> updateCard(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @Valid @RequestBody final ConsumerCardRequestDTO consumerCardDTO )
    {
        service.updateCard( consumerId, consumerCardDTO );
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<EntityPageableDTO<ConsumerCardRequestDTO>> findConsumersCards(
        @PathVariable( "consumer_id" ) final Integer consumerId )
    {
        return ResponseEntity.ok( service.findConsumersCards( consumerId ) );
    }

    @PutMapping( "/{card_id}/credits-balances" )
    public ResponseEntity<Void> creditCardBalance(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId,
        @RequestParam( "credit_value" ) final Long creditValue )
    {
        service.creditCardBalance( consumerId, cardId, creditValue );
        return ResponseEntity.noContent().build();
    }

    @PostMapping( "/{card_id}/debits-balances" )
    public ResponseEntity<CardDebitBalanceResponseDTO> debitCardBalance(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId,
        @Valid @RequestBody final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {
        return ResponseEntity.ok( service.debitCardBalance( consumerId, cardId, cardDebitBalanceRequestDTO ) );
    }
}
