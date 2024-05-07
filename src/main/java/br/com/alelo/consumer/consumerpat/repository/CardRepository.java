package br.com.alelo.consumer.consumerpat.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.alelo.consumer.consumerpat.model.card.PersistentConsumerCard;

@Repository
public interface CardRepository
    extends
        JpaRepository<PersistentConsumerCard,Integer>
{
    Page<PersistentConsumerCard> findByConsumerId(
        Integer consumerId,
        Pageable pageable );

    Optional<PersistentConsumerCard> findByIdAndConsumerId(
        Integer id,
        Integer consumerId );

    boolean existsByNumber(
        Long number );
}
