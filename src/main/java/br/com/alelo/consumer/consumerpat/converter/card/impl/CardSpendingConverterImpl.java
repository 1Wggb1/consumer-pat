package br.com.alelo.consumer.consumerpat.converter.card.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import br.com.alelo.consumer.consumerpat.converter.card.CardSpendingConverter;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitProductDTO;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpendingProduct;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;

@Component
public class CardSpendingConverterImpl
    implements
        CardSpendingConverter
{
    @Override
    public PersistentCardSpending toModel(
        final BigDecimal totalDebit,
        final PersistentConsumerCard consumerCard,
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO )
    {
        final PersistentCardSpending persistentCardSpending = new PersistentCardSpending(
            cardDebitBalanceRequestDTO.establishmentName(),
            consumerCard.getEstablishmentType(),
            consumerCard,
            totalDebit );
        persistentCardSpending.getProducts()
            .addAll( createSpendingProducts( cardDebitBalanceRequestDTO, persistentCardSpending ) );
        return persistentCardSpending;
    }

    private static List<PersistentCardSpendingProduct> createSpendingProducts(
        final CardDebitBalanceRequestDTO cardDebitBalanceRequestDTO,
        final PersistentCardSpending persistentCardSpending )
    {
        final List<CardDebitProductDTO> cardDebitProducts = cardDebitBalanceRequestDTO.debitProducts();
        return cardDebitProducts.stream()
            .map( dto -> createSpendingProduct( dto, persistentCardSpending ) )
            .toList();
    }

    private static PersistentCardSpendingProduct createSpendingProduct(
        final CardDebitProductDTO cardDebitProductDTO,
        final PersistentCardSpending persistentCardSpending )
    {
        return new PersistentCardSpendingProduct( cardDebitProductDTO.productName(),
            cardDebitProductDTO.quantity(),
            cardDebitProductDTO.unitaryPrice(),
            persistentCardSpending );
    }

    @Override
    public CardDebitBalanceResponseDTO toDTO(
        final PersistentCardSpending cardSpending )
    {
        return new CardDebitBalanceResponseDTO( cardSpending.getId() );
    }
}
