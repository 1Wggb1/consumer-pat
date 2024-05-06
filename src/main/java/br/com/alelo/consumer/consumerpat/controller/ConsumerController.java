package br.com.alelo.consumer.consumerpat.controller;

import java.net.URI;
import java.util.List;

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
import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping( "v1/consumers" )
public class ConsumerController
{
    @Autowired
    private ConsumerService service;

    @GetMapping
    public ResponseEntity<List<Consumer>> findConsumers(
        @RequestParam( name = "page", defaultValue = "0" ) final Integer page,
        @RequestParam( name = "size", defaultValue = "10" ) final Integer size )
    {
        final PageRequest pageable = PageRequest.of( page, size );
        return ResponseEntity.ok( service.findConsumers( pageable ) );
    }

    @PostMapping
    public ResponseEntity<Consumer> createConsumer(
        @RequestBody final Consumer consumer )
    {
        final Consumer createdConsumer = service.create( consumer );
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .buildAndExpand( createdConsumer.getId() )
            .toUri();
        return ResponseEntity.created( location ).body( createdConsumer );
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<Void> updateConsumer(
        @PathVariable( "id" ) final Integer id,
        @RequestBody final Consumer consumer )
    {
        service.update( id, consumer );
        return ResponseEntity.noContent().build();
    }

    @PutMapping( "/cardbalance" )
    public ResponseEntity<Void> setBalance(
        final Integer cardNumber,
        final Double value )
    {
        service.setBalance( cardNumber, value );
        return ResponseEntity.noContent().build();
    }

    @PostMapping( "/buy" )
    public ResponseEntity<Void> buy(
        final Integer establishmentType,
        final String establishmentName,
        final Integer cardNumber,
        final String productDescription,
        final Double value )
    {
        service.buy( establishmentType, establishmentName, cardNumber, productDescription, value );
        return ResponseEntity.ok().build();
    }
}
