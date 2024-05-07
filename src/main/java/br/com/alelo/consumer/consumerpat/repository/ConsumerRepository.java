package br.com.alelo.consumer.consumerpat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;

@Repository
public interface ConsumerRepository
    extends
        JpaRepository<PersistentConsumer,Integer>
{
}
