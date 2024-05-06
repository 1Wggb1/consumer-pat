package br.com.alelo.consumer.consumerpat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.alelo.consumer.consumerpat.entity.Consumer;
import br.com.alelo.consumer.consumerpat.entity.Extract;
import br.com.alelo.consumer.consumerpat.respository.ConsumerRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import br.com.alelo.consumer.consumerpat.service.ConsumerService;

@Service
public class CustomerServiceImpl
    implements
        ConsumerService
{
    @Autowired
    private ConsumerRepository repository;
    @Autowired
    private ExtractRepository extractRepository;

    /* Cadastrar novos clientes */
    @Override
    public Consumer create(
        final Consumer consumer )
    {
        repository.save( consumer );
        return null;
    }

    /*
     * Listar todos os clientes (obs.: tabela possui cerca de 50.000 registros)
     */
    @Override
    public List<Consumer> findConsumers(
        final Pageable pageable )
    {
        return repository.getAllConsumersList();
    }

    // Atualizar cliente, lembrando que não deve ser possível alterar o saldo do
    // cartão
    @Override
    public Consumer update(
        final Integer id,
        final Consumer consumer )
    {
        repository.save( consumer );
        return null;
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
        Consumer consumer = null;
        consumer = repository.findByDrugstoreNumber( cardNumber );

        if( consumer != null ) {
            // é cartão de farmácia
            consumer.setDrugstoreCardBalance( consumer.getDrugstoreCardBalance() + value );
            repository.save( consumer );
        } else {
            consumer = repository.findByFoodCardNumber( cardNumber );
            if( consumer != null ) {
                // é cartão de refeição
                consumer.setFoodCardBalance( consumer.getFoodCardBalance() + value );
                repository.save( consumer );
            } else {
                // É cartão de combustivel
                consumer = repository.findByFuelCardNumber( cardNumber );
                consumer.setFuelCardBalance( consumer.getFuelCardBalance() + value );
                repository.save( consumer );
            }
        }
    }

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
        Consumer consumer = null;
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
            consumer.setFoodCardBalance( consumer.getFoodCardBalance() - value );
            repository.save( consumer );

        } else if( establishmentType == 2 ) {
            consumer = repository.findByDrugstoreNumber( cardNumber );
            consumer.setDrugstoreCardBalance( consumer.getDrugstoreCardBalance() - value );
            repository.save( consumer );

        } else {
            // Nas compras com o cartão de combustivel existe um acrescimo de
            // 35%;
            final Double tax = ( value / 100 ) * 35;
            value = value + tax;

            consumer = repository.findByFuelCardNumber( cardNumber );
            consumer.setFuelCardBalance( consumer.getFuelCardBalance() - value );
            repository.save( consumer );
        }

        final Extract extract = new Extract( establishmentName, productDescription, new Date(), cardNumber, value );
        extractRepository.save( extract );
    }
}
