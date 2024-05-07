package br.com.alelo.consumer.consumerpat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.alelo.consumer.consumerpat.dto.EntityPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.card.CardDebitBalanceResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.card.ConsumerCardRequestDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardRepository;
import br.com.alelo.consumer.consumerpat.repository.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerCardService;

@Service
public class ConsumerCardServiceImpl
    implements
        ConsumerCardService
{
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;

    @Override
    public CardDebitBalanceResponseDTO createCard(
        final Integer consumerId,
        final ConsumerCardRequestDTO consumerCardDTO )
    {
        return null;
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do
    // cartão
    // update?
    @Override
    public void updateCard(
        final Integer consumerId,
        final ConsumerCardRequestDTO consumerCardDTO )
    {

    }

    @Override
    public EntityPageableDTO<ConsumerCardRequestDTO> findConsumersCards(
        final Integer consumerId )
    {
        return null;
    }

    /*
     * Credito de valor no cartão cardNumber: número do cartão value: valor a
     * ser creditado (adicionado ao saldo)
     */
    @Override
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
