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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInvalidCreditValueException;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table( name = "consumer_card" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentConsumerCard
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Integer id;

    @NotNull
    @Column( name = "number", nullable = false, unique = true )
    private Long number;

    @PositiveOrZero
    @Column( name = "balance_cents" )
    private Long balanceCents;

    @NotNull
    @Column( name = "establishment_type", nullable = false )
    @Enumerated( EnumType.STRING )
    private CardEstablishmentType establishmentType;

    @NotNull
    @ManyToOne
    @JoinColumn( name = "consumer_id", nullable = false )
    private PersistentConsumer consumer;

    public void setId(
        final Integer id )
    {
        throw new UnsupportedOperationException( "Id cannot be changed." );
    }

    public void addCredit(
        final Long creditCents )
    {
        validateBalance( creditCents );
        this.balanceCents += creditCents;
    }

    private void validateBalance(
        final Long creditCents )
    {
        if( creditCents == null || creditCents < 0 ) {
            throw new ConsumerCardInvalidCreditValueException( id, consumer.getId() );
        }
    }

    public void setBalanceCents(
        final Long balanceCents )
    {
        validateBalance( balanceCents );
        this.balanceCents += balanceCents;
    }
}
