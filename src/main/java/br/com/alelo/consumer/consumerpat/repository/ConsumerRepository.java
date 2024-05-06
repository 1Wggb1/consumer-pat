package br.com.alelo.consumer.consumerpat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;

@Repository
public interface ConsumerRepository
    extends
        JpaRepository<PersistentConsumer,Integer>
{
    @Query( nativeQuery = true, value = "select * from Consumer where FOOD_CARD_NUMBER = ? " )
    PersistentConsumer findByFoodCardNumber(
        int cardNumber );

    @Query( nativeQuery = true, value = "select * from Consumer where FUEL_CARD_NUMBER = ? " )
    PersistentConsumer findByFuelCardNumber(
        int cardNumber );

    @Query( nativeQuery = true, value = "select * from Consumer where DRUGSTORE_NUMBER = ? " )
    PersistentConsumer findByDrugstoreNumber(
        int cardNumber );

}
