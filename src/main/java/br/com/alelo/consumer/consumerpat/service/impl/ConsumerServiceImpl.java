package br.com.alelo.consumer.consumerpat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.alelo.consumer.consumerpat.dto.ConsumerDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerPageableDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.model.PersistentCardSpending;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.CardSpendingRepository;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;

@Service
public class ConsumerServiceImpl
    implements
        ConsumerService
{
    @Autowired
    private ConsumerRepository repository;
    @Autowired
    private CardSpendingRepository cardSpendingRepository;

    @Override
    public ConsumerResponseDTO create(
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final PersistentConsumer persistentConsumer = PersistentConsumer.builder()
            .name( consumerRequestDTO.name() )
            .documentNumber( consumerRequestDTO.documentNumber() )
            .birthday( consumerRequestDTO.birthday() )
            .contact( consumerRequestDTO.contact() )
            .address( consumerRequestDTO.address() )
            .build();
        final PersistentConsumer saved = repository.save( persistentConsumer );
        return new ConsumerResponseDTO( saved.getId() );
    }

    @Override
    public ConsumerPageableDTO findConsumers(
        final Pageable pageable )
    {
        final Page<PersistentConsumer> consumersPage = repository.findAll( pageable );
        final PageableDTO pageableDTO = new PageableDTO(
            consumersPage.getNumber(),
            consumersPage.getSize(),
            consumersPage.getNumberOfElements(),
            consumersPage.getTotalPages(),
            (int) consumersPage.getTotalElements() );
        return new ConsumerPageableDTO( pageableDTO, toConsumerDTO( consumersPage.getContent() ) );
    }

    private static List<ConsumerDTO> toConsumerDTO(
        final List<PersistentConsumer> persistentConsumers )
    {
        return persistentConsumers.stream()
            .map( ConsumerServiceImpl::toConsumerDTO )
            .toList();
    }

    private static ConsumerDTO toConsumerDTO(
        final PersistentConsumer persistentConsumer )
    {
        return new ConsumerDTO( persistentConsumer.getId(),
            persistentConsumer.getName(),
            persistentConsumer.getDocumentNumber(),
            persistentConsumer.getBirthday(),
            persistentConsumer.getContact(),
            persistentConsumer.getAddress() );
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do
    // cartão
    @Override
    public void update(
        final Integer id,
        final ConsumerRequestDTO consumerRequestDTO )
    {
        final PersistentConsumer persistentConsumer = repository.findById( id )
            .orElseThrow();
        persistentConsumer.setName( consumerRequestDTO.name() );
        persistentConsumer.setBirthday( consumerRequestDTO.birthday() );
        persistentConsumer.setDocumentNumber( consumerRequestDTO.documentNumber() );
        persistentConsumer.setContact( consumerRequestDTO.contact() );
        persistentConsumer.setAddress( consumerRequestDTO.address() );
        repository.save( persistentConsumer );
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
