package br.com.alelo.consumer.consumerpat.controller.card;

import java.net.URI;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import br.com.alelo.consumer.consumerpat.dto.card.CardCreditBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
import br.com.alelo.consumer.consumerpat.service.card.ConsumerCardService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping( "/v1/consumers/{consumer_id}/cards" )
public class ConsumerCardController
{
    @Autowired
    private ConsumerCardService service;

    @PostMapping
    public ResponseEntity<ConsumerCardResponseDTO> createCard(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @Valid @RequestBody final ConsumerCardRequestDTO consumerCardDTO )
    {
        final ConsumerCardResponseDTO responseDTO = service.createCard( consumerId, consumerCardDTO );
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .buildAndExpand( responseDTO.id() )
            .toUri();
        return ResponseEntity.created( location ).body( responseDTO );
    }

    @PutMapping( "/{card_id}" )
    public ResponseEntity<Void> updateCard(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId,
        @Valid @RequestBody final ConsumerCardUpdateRequestDTO consumerCardDTO )
    {
        service.updateCard( consumerId, cardId, consumerCardDTO );
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<EntityPageableDTO<ConsumerCardDTO>> findConsumersCards(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @RequestParam( name = "page", defaultValue = "0" ) final Integer page,
        @RequestParam( name = "size", defaultValue = "10" ) final Integer size )
    {
        final PageRequest pageable = PageRequest.of( page, size );
        return ResponseEntity.ok( service.findConsumersCards( consumerId, pageable ) );
    }

    @PutMapping( "/{card_id}/credits-balances" )
    public ResponseEntity<Void> creditCardBalance(
        @PathVariable( "consumer_id" ) final Integer consumerId,
        @PathVariable( "card_id" ) final Integer cardId,
        @Valid @RequestBody final CardCreditBalanceRequestDTO creditBalanceRequestDTO )
    {
        service.creditCardBalance( consumerId, cardId, creditBalanceRequestDTO );
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
