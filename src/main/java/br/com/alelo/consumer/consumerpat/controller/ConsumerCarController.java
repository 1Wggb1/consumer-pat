package br.com.alelo.consumer.consumerpat.controller;

import java.net.URI;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping( "/v1/consumers/{consumer_id}/cards" )
public class ConsumerCarController
{
    @PostMapping
    public ResponseEntity<CardDebitBalanceResponseDTO> createCard(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @Valid final ConsumerCardRequestDTO consumerCardDTO )
    {
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .buildAndExpand( 1 )
            .toUri();
        return ResponseEntity.created( location ).body( null );
    }

    @GetMapping
    public ResponseEntity<EntityPageableDTO<ConsumerCardRequestDTO>> findConsumersCards(
        @PathVariable( "consumer_id" ) final Integer consumerId )
    {

        return ResponseEntity.ok( null );
    }

    @PutMapping( "/{card_id}/credits-balances" )
    public ResponseEntity<Void> creditCardBalance(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId )
    {

        return ResponseEntity.noContent().build();
    }

    @PostMapping( "/{card_id}/debits-balances" )
    public ResponseEntity<CardDebitBalanceResponseDTO> debitCardBalancce(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId,
        @Valid final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {

        return ResponseEntity.ok().build();
    }
}
