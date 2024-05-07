package br.com.alelo.consumer.consumerpat.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.alelo.consumer.consumerpat.converter.CardConverter;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
import br.com.alelo.consumer.consumerpat.exception.CardInvalidConsumerException;
import br.com.alelo.consumer.consumerpat.exception.CardNotFoundException;
import br.com.alelo.consumer.consumerpat.exception.ConsumerCardsNotFoundException;
import br.com.alelo.consumer.consumerpat.exception.ConsumerNotFoundException;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardRepository;
import br.com.alelo.consumer.consumerpat.repository.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerCardService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional( readOnly = true )
@Service
public class ConsumerCardServiceImpl
    implements
        ConsumerCardService
{
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;
    @Autowired
    private CardConverter cardConverter;

    @Override
    @Transactional
    public ConsumerCardResponseDTO createCard(
        final Integer consumerId,
        final ConsumerCardRequestDTO consumerCardDTO )
    {
        log.info( "Creating... card to consumer id = {}", consumerId );
        final PersistentConsumer persistentConsumer = findConsumerOrThrownException( consumerId );
        final PersistentConsumerCard savedPersistent = cardRepository.save( cardConverter.toModel( consumerCardDTO, persistentConsumer ) );
        log.info( "Consumer with id = {} had card with id = {} created successfully",
            consumerId, savedPersistent.getId() );
        return cardConverter.toResponseDTO( savedPersistent );
    }

    private PersistentConsumer findConsumerOrThrownException(
        final Integer consumerId )
    {
        return consumerRepository.findById( consumerId )
            .orElseThrow( () -> new ConsumerNotFoundException( consumerId ) );
    }

    @Override
    @Transactional
    public void updateCard(
        final Integer consumerId,
        final Integer cardId,
        final ConsumerCardUpdateRequestDTO cardUpdateRequestDTO )
    {
        log.info( "Updating... card to consumer id = {}", consumerId );
        final PersistentConsumerCard persistentConsumerCard = cardRepository.findById( cardId )
            .orElseThrow( () -> new CardNotFoundException( cardId ) );
        final Integer cardConsumerId = persistentConsumerCard.getConsumer().getId();
        validateConsumerId( consumerId, cardConsumerId );
        log.info( "Consumer with id = {} had card with id = {} updated successfully",
            consumerId, cardId );
        cardRepository.save( cardConverter.toModel( persistentConsumerCard, cardUpdateRequestDTO ) );
    }

    private static void validateConsumerId(
        final Integer consumerId,
        final Integer cardConsumerId )
    {
        if( ! Objects.equals( consumerId, cardConsumerId ) ) {
            throw new CardInvalidConsumerException();
        }
    }

    @Override
    public EntityPageableDTO<ConsumerCardDTO> findConsumersCards(
        final Integer consumerId,
        final Pageable pageable )
    {
        log.info( "Finding.... cards from consumer id = {} and pageable = {}", consumerId, pageable );
        final Page<PersistentConsumerCard> consumerCardsPage = cardRepository.findByConsumerId( consumerId, pageable );
        validateIfCardsExists( consumerId, consumerCardsPage );
        log.info( "Cards found from consumer id = {}", consumerId );
        return cardConverter.toPageableDTO( consumerCardsPage );
    }

    private static void validateIfCardsExists(
        final Integer consumerId,
        final Page<PersistentConsumerCard> consumerCardsPage )
    {
        if( consumerCardsPage.getTotalElements() == 0L ) {
            throw new ConsumerCardsNotFoundException( consumerId );
        }
    }

    /*
     * Credito de valor no cartão cardNumber: número do cartão value: valor a
     * ser creditado (adicionado ao saldo)
     */
    @Override
    @Transactional
    public void creditCardBalance(
        final Integer consumerId,
        final Integer cardId,
        final Long creditValue )
    {
        final PersistentConsumer consumer = null;
        // consumer = repository.findByDrugstoreNumber( cardNumber );

        if( consumer != null ) {
            // é cartão de farmácia
            // consumer.setDrugstoreCardBalance(
            // consumer.getDrugstoreCardBalance() + value );
            // repository.save( consumer );
        } else {
            // consumer = repository.findByFoodCardNumber( cardNumber );
            if( consumer != null ) {
                // é cartão de refeição
                // consumer.setFoodCardBalance( consumer.getFoodCardBalance() +
                // value );
                // repository.save( consumer );
            } else {
                // É cartão de combustivel
                // consumer = repository.findByFuelCardNumber( cardNumber );
                // consumer.setFuelCardBalance( consumer.getFuelCardBalance() +
                // value );
                // repository.save( consumer );
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
    @Transactional
    public CardDebitBalanceResponseDTO debitCardBalance(
        final Integer consumerId,
        final Integer cardId,
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {
        final PersistentConsumer consumer = null;
        /*
         * O valor só podem ser debitado do catão com o tipo correspondente ao
         * tipo do estabelecimento da compra. Exemplo: Se a compra é em um
         * estabelecimeto de Alimentação (food) então o valor só pode ser
         * debitado do cartão alimentação Tipos dos estabelcimentos: 1)
         * Alimentação (Food) 2) Farmácia (DrugStore) 3) Posto de combustivel
         * (Fuel)
         */

        if( 1 == 1 ) {
            // Para compras no cartão de alimentação o cliente recebe um
            // desconto de 10%
            // final Double cashback = ( value / 100 ) * 10;
            // value = value - cashback;

            // consumer = repository.findByFoodCardNumber( cardNumber );
            // consumer.setFoodCardBalance( consumer.getFoodCardBalance() -
            // value );
            // repository.save( consumer );

        } else if( 2 == 2 ) {
            // consumer = repository.findByDrugstoreNumber( cardNumber );
            // consumer.setDrugstoreCardBalance(
            // consumer.getDrugstoreCardBalance() - value );
            // repository.save( consumer );

        } else {
            // Nas compras com o cartão de combustivel existe um acrescimo de
            // 35%;
            // final Double tax = ( value / 100 ) * 35;
            // value = value + tax;

            // consumer = repository.findByFuelCardNumber( cardNumber );
            // consumer.setFuelCardBalance( consumer.getFuelCardBalance() -
            // value );
            // repository.save( consumer );
        }

        // final PersistentCardSpending extract = new PersistentCardSpending(
        // establishmentName,
        // productDescription,
        // cardNumber,
        // value );
        // cardSpendingRepository.save( extract );
        return null;
    }
}
