package br.com.alelo.consumer.consumerpat.service.card.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.alelo.consumer.consumerpat.converter.card.CardSpendingConverter;
import br.com.alelo.consumer.consumerpat.converter.card.ConsumerCardConverter;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardCreditBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardUpdateRequestDTO;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInvalidConsumerException;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardNotAcceptedEstablishmentException;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardNotFoundException;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardNumberAlreadyExists;
import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardsNotFoundException;
import br.com.alelo.consumer.consumerpat.exception.consumer.ConsumerNotFoundException;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;
import br.com.alelo.consumer.consumerpat.model.card.calculator.DebitBalanceCalculator;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.card.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.repository.card.ConsumerCardRepository;
import br.com.alelo.consumer.consumerpat.repository.consumer.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.service.card.ConsumerCardService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional( readOnly = true )
@Service
public class ConsumerCardServiceImpl
    implements
        ConsumerCardService
{
    @Autowired
    private ConsumerCardRepository consumerCardRepository;
    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;
    @Autowired
    private ConsumerCardConverter consumerCardConverter;
    @Autowired
    private CardSpendingConverter cardSpendingConverter;

    @Override
    @Transactional
    public ConsumerCardResponseDTO createCard(
        final Integer consumerId,
        final ConsumerCardRequestDTO consumerCardDTO )
    {
        log.info( "Creating... card to consumer id = {}", consumerId );
        final PersistentConsumer persistentConsumer = findConsumerOrThrownException( consumerId );
        validateIfUniqueCardNumber( consumerCardDTO.number() );
        final PersistentConsumerCard savedPersistent = consumerCardRepository.save( consumerCardConverter.toModel( consumerCardDTO,
            persistentConsumer ) );
        log.info( "Consumer with id = {} had card with id = {} created successfully",
            consumerId, savedPersistent.getId() );
        return consumerCardConverter.toResponseDTO( savedPersistent );
    }

    private void validateIfUniqueCardNumber(
        final Long cardNumber )
    {
        final boolean existByCardNumber = consumerCardRepository.existsByNumber( cardNumber );
        if( existByCardNumber ) {
            throw new ConsumerCardNumberAlreadyExists();
        }
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
        final PersistentConsumerCard persistentConsumerCard = consumerCardRepository.findById( cardId )
            .orElseThrow( () -> new ConsumerCardNotFoundException( cardId ) );
        validateIfUniqueCardNumber( cardUpdateRequestDTO.number() );
        final Integer cardConsumerId = persistentConsumerCard.getConsumer().getId();
        validateConsumerId( consumerId, cardConsumerId );
        log.info( "Consumer with id = {} had card with id = {} updated successfully",
            consumerId, cardId );
        consumerCardRepository.save( consumerCardConverter.toModel( persistentConsumerCard, cardUpdateRequestDTO ) );
    }

    private static void validateConsumerId(
        final Integer consumerId,
        final Integer cardConsumerId )
    {
        if( ! Objects.equals( consumerId, cardConsumerId ) ) {
            throw new ConsumerCardInvalidConsumerException();
        }
    }

    @Override
    public EntityPageableDTO<ConsumerCardDTO> findConsumersCards(
        final Integer consumerId,
        final Pageable pageable )
    {
        log.info( "Finding.... cards from consumer id = {} and pageable = {}", consumerId, pageable );
        final Page<PersistentConsumerCard> consumerCardsPage = consumerCardRepository.findByConsumerId( consumerId, pageable );
        validateIfCardsExists( consumerId, consumerCardsPage );
        log.info( "Cards found from consumer id = {}", consumerId );
        return consumerCardConverter.toPageableDTO( consumerCardsPage );
    }

    private static void validateIfCardsExists(
        final Integer consumerId,
        final Page<PersistentConsumerCard> consumerCardsPage )
    {
        if( consumerCardsPage.getTotalElements() == 0L ) {
            throw new ConsumerCardsNotFoundException( consumerId );
        }
    }

    @Override
    @Transactional
    public void creditCardBalance(
        final Integer consumerId,
        final Integer cardId,
        final CardCreditBalanceRequestDTO creditBalanceRequestDTO )
    {
        log.info( "Adding credit value.... to consumer id = {} and card id= {}", consumerId, cardId );
        final PersistentConsumerCard consumerCard = findCardByIdAndConsumerIdOrThrowException( cardId, consumerId );
        final Long creditCents = creditBalanceRequestDTO.creditCents();
        consumerCard.addCredit( creditCents );
        consumerCardRepository.save( consumerCard );
        log.info( "Credit value = {} added successfully to consumer id = {} and card id = {}",
            creditCents, consumerId, cardId );
    }

    private PersistentConsumerCard findCardByIdAndConsumerIdOrThrowException(
        final Integer cardId,
        final Integer consumerId )
    {
        return consumerCardRepository
            .findByIdAndConsumerId( cardId, consumerId )
            .orElseThrow( () -> new ConsumerCardNotFoundException( cardId ) );
    }

    @Override
    @Transactional
    public CardDebitBalanceResponseDTO debitCardBalance(
        final Integer consumerId,
        final Integer cardId,
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {
        log.info( "Debiting... value from consumer id = {} and card id = {}", consumerId, cardId );
        final PersistentConsumerCard consumerCard = findCardByIdAndConsumerIdOrThrowException( cardId, consumerId );
        final CardEstablishmentType consumerCardEstablishmentType = consumerCard.getEstablishmentType();
        validateEstablishmentType( consumerCardEstablishmentType, cardDebitBalanceRequestDTO );

        final DebitBalanceCalculator debitBalanceCalculator = consumerCardEstablishmentType.getDebitBalanceCalculator();
        final Long totalDebit = debitBalanceCalculator.calculateTotal( cardDebitBalanceRequestDTO.debitValueCents() );
        consumerCard.debit( totalDebit );
        consumerCardRepository.save( consumerCard );

        log.info( "Debit from consumer id = {} and card id = {} realized successfully!", consumerId, cardId );
        return createCardSpending( cardDebitBalanceRequestDTO, totalDebit, consumerCard );
    }

    private static void validateEstablishmentType(
        final CardEstablishmentType consumerCardEstablishmentType,
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {
        log.info( "Validating... card and establishment type" );
        final CardEstablishmentType debitEstablishmentType = CardEstablishmentType
            .getOrThrownException( cardDebitBalanceRequestDTO.establishmentType() );
        if( ! consumerCardEstablishmentType.equals( debitEstablishmentType ) ) {
            throw new ConsumerCardNotAcceptedEstablishmentException( consumerCardEstablishmentType, debitEstablishmentType );
        }
    }

    private CardDebitBalanceResponseDTO createCardSpending(
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO,
        final Long totalDebit,
        final PersistentConsumerCard consumerCard )
    {
        final PersistentCardSpending cardSpending = cardSpendingConverter.toModel( totalDebit, consumerCard, cardDebitBalanceRequestDTO );
        return cardSpendingConverter.toDTO( cardSpendingRepository.save( cardSpending ) );
    }
}
