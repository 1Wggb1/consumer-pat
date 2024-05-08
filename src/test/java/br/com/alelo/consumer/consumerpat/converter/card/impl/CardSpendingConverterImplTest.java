package br.com.alelo.consumer.consumerpat.converter.card.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitProductDTO;
import br.com.alelo.consumer.consumerpat.model.card.CardEstablishmentType;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.card.PersistentCardSpendingProduct;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;

class CardSpendingConverterImplTest
{
    private final CardSpendingConverterImpl subject = new CardSpendingConverterImpl();

    @Test
    @DisplayName( "Deve retornar persistent de gasto do débito do cartão a partir do DTO." )
    void shouldReturnModelFromDTO()
    {
        final PersistentConsumerCard consumerCard = PersistentConsumerCard.builder()
            .id( 1 )
            .establishmentType( CardEstablishmentType.FOOD )
            .build();
        final List<CardDebitProductDTO> debitProductsDTO = List.of( createDebitProductDTO( "Maçã Gala" ), createDebitProductDTO(
            "Feijão preto" ) );
        final long totalDebit = 1000L;
        final CardDebitBalanceRequestDTO debitBalanceRequestDTO = new CardDebitBalanceRequestDTO( CardEstablishmentType.FOOD.name(),
            "Padaria S.A.", debitProductsDTO, totalDebit );

        final PersistentCardSpending persistentCardSpending = subject.toModel( totalDebit, consumerCard, debitBalanceRequestDTO );

        assertEquals( consumerCard, persistentCardSpending.getConsumerCard() );
        assertEquals( debitBalanceRequestDTO.establishmentType(),
            persistentCardSpending.getEstablishmentType().name() );
        assertEquals( debitBalanceRequestDTO.establishmentName(), persistentCardSpending.getEstablishmentName() );
        assertEquals( totalDebit, persistentCardSpending.getAmountCents() );
        assertNotNull( persistentCardSpending.getPurchaseDateTime() );
        validateDebitProducts( debitProductsDTO, persistentCardSpending );
    }

    private static CardDebitProductDTO createDebitProductDTO(
        final String name )
    {
        return new CardDebitProductDTO( name, 1L, 1L );
    }

    private static void validateDebitProducts(
        final List<CardDebitProductDTO> cardDebitProductDTOS,
        final PersistentCardSpending persistentCardSpending )
    {
        assertEquals( cardDebitProductDTOS.size(), persistentCardSpending.getProducts().size() );
        final List<PersistentCardSpendingProduct> products = persistentCardSpending.getProducts();
        assertEquals( createDebitProduct( cardDebitProductDTOS.get( 0 ), persistentCardSpending ), products.get( 0 ) );
        assertEquals( createDebitProduct( cardDebitProductDTOS.get( 1 ), persistentCardSpending ), products.get( 1 ) );
    }

    private static PersistentCardSpendingProduct createDebitProduct(
        final CardDebitProductDTO cardDebitProductDTO,
        final PersistentCardSpending persistentCardSpending )
    {
        return new PersistentCardSpendingProduct( cardDebitProductDTO.productName(),
            cardDebitProductDTO.quantity(),
            cardDebitProductDTO.unitaryPriceCents(),
            persistentCardSpending );
    }

    @Test
    @DisplayName( "Deve retornar dto de resposta a partir do persistent." )
    void shouldReturnResponseDTOFromPersistent()
    {
        final PersistentCardSpending persistentCardSpending = PersistentCardSpending.builder()
            .id( 1 )
            .build();

        assertEquals( subject.toDTO( persistentCardSpending ), new CardDebitBalanceResponseDTO( persistentCardSpending.getId() ) );
    }
}
