package br.com.alelo.consumer.consumerpat.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.alelo.consumer.consumerpat.converter.ConsumerConverter;
import br.com.alelo.consumer.consumerpat.dto.ConsumerPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerResponseDTO;
import br.com.alelo.consumer.consumerpat.exception.ConsumerNotFoundException;
import br.com.alelo.consumer.consumerpat.model.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ConsumerServiceImpl
    implements
        ConsumerService
{
    @Autowired
    private ConsumerRepository repository;
    @Autowired
    private ConsumerConverter converter;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;

    @Override
    public ConsumerResponseDTO create(
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        final String documentNumber = consumerRequestDTO.documentNumber();
        logWithTrace( traceId, String.format( "Creating consumer with documentNumber = %s...", documentNumber ) );
        final PersistentConsumer saved = repository.save( converter.toModel( consumerRequestDTO ) );
        logWithTrace( traceId, String.format( "Consumer with  id = %d and  documentNumber = %s created successfully!",
            saved.getId(), documentNumber ) );
        return new ConsumerResponseDTO( saved.getId() );
    }

    private static void logWithTrace(
        final UUID traceId,
        final String logMessage )
    {
        log.info( "TRACEID = {} - {}", traceId, logMessage );
    }

    @Override
    public ConsumerPageableDTO findConsumers(
        final Pageable pageable )
    {
        final UUID traceId = UUID.randomUUID();
        logWithTrace( traceId, String.format( "Finding consumers by %s", pageable ) );
        final Page<PersistentConsumer> consumersPage = repository.findAll( pageable );
        logWithTrace( traceId, String.format( "Consumers found by %s", pageable ) );
        return converter.toPageableDTO( consumersPage );
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do
    // cartão
    @Override
    public void update(
        final Integer id,
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        logWithTrace( traceId, String.format( "Updating consumer by id = %d", id ) );
        final PersistentConsumer persistentConsumer = repository.findById( id )
            .orElseThrow( () -> new ConsumerNotFoundException( id ) );
        final PersistentConsumer updatedConsumer = converter.toModel( persistentConsumer, consumerRequestDTO );
        repository.save( updatedConsumer );
        logWithTrace( traceId, String.format( "Consumer with id = %d updated successfully!", id ) );
    }

    /*
     * Credito de valor no cartão cardNumber: número do cartão value: valor a
     * ser creditado (adicionado ao saldo)
     */
    @Override
    public void setBalance(
        final Integer cardNumber,
        final Double value )
    {
        PersistentConsumer consumer = null;
        consumer = repository.findByDrugstoreNumber( cardNumber );

        if( consumer != null ) {
            // é cartão de farmácia
            // consumer.setDrugstoreCardBalance(
            // consumer.getDrugstoreCardBalance() + value );
            repository.save( consumer );
        } else {
            consumer = repository.findByFoodCardNumber( cardNumber );
            if( consumer != null ) {
                // é cartão de refeição
                // consumer.setFoodCardBalance( consumer.getFoodCardBalance() +
                // value );
                repository.save( consumer );
            } else {
                // É cartão de combustivel
                consumer = repository.findByFuelCardNumber( cardNumber );
                // consumer.setFuelCardBalance( consumer.getFuelCardBalance() +
                // value );
                repository.save( consumer );
            }
        }
    }

    // Criar modelo
    // Criar enum com tipo do estabelecimento
    // enum ter o algoritmo para calculo de taxa
    /*
     * Débito de valor no cartão (compra) establishmentType: tipo do
     * estabelecimento comercial establishmentName: nome do estabelecimento
     * comercial cardNumber: número do cartão productDescription: descrição do
     * produto value: valor a ser debitado (subtraído)
     */
    @Override
    public void buy(
        final Integer establishmentType,
        final String establishmentName,
        final Integer cardNumber,
        final String productDescription,
        Double value )
    {
        PersistentConsumer consumer = null;
        /*
         * O valor só podem ser debitado do catão com o tipo correspondente ao
         * tipo do estabelecimento da compra. Exemplo: Se a compra é em um
         * estabelecimeto de Alimentação (food) então o valor só pode ser
         * debitado do cartão alimentação Tipos dos estabelcimentos: 1)
         * Alimentação (Food) 2) Farmácia (DrugStore) 3) Posto de combustivel
         * (Fuel)
         */

        if( establishmentType == 1 ) {
            // Para compras no cartão de alimentação o cliente recebe um
            // desconto de 10%
            final Double cashback = ( value / 100 ) * 10;
            value = value - cashback;

            consumer = repository.findByFoodCardNumber( cardNumber );
            // consumer.setFoodCardBalance( consumer.getFoodCardBalance() -
            // value );
            repository.save( consumer );

        } else if( establishmentType == 2 ) {
            consumer = repository.findByDrugstoreNumber( cardNumber );
            // consumer.setDrugstoreCardBalance(
            // consumer.getDrugstoreCardBalance() - value );
            repository.save( consumer );

        } else {
            // Nas compras com o cartão de combustivel existe um acrescimo de
            // 35%;
            final Double tax = ( value / 100 ) * 35;
            value = value + tax;

            consumer = repository.findByFuelCardNumber( cardNumber );
            // consumer.setFuelCardBalance( consumer.getFuelCardBalance() -
            // value );
            repository.save( consumer );
        }

        final PersistentCardSpending extract = new PersistentCardSpending(
            establishmentName,
            productDescription,
            cardNumber,
            value );
        cardSpendingRepository.save( extract );
    }
}
