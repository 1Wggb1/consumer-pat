package br.com.alelo.consumer.consumerpat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumerCard;

@Repository
public interface CardRepository
    extends
        JpaRepository<PersistentConsumerCard,Integer>
{
}
