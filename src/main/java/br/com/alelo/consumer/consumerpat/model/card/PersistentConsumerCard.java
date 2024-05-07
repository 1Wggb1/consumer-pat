package br.com.alelo.consumer.consumerpat.model.card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table( name = "consumer_card" )
@NoArgsConstructor
@AllArgsConstructor
public class PersistentConsumerCard
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Integer id;

    @Column( name = "number", nullable = false )
    private Long number;

    @Column( name = "balance_cents" )
    private Long balanceCents;

    @Column( name = "establishment_type" )
    @Enumerated( EnumType.STRING )
    private CardEstablishmentType establishmentType;

    @ManyToOne
    @JoinColumn( name = "consumer_id", nullable = false )
    private PersistentConsumer consumer;
}
