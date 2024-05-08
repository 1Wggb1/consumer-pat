package br.com.alelo.consumer.consumerpat.model.card;

import java.math.BigDecimal;
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

import br.com.alelo.consumer.consumerpat.exception.card.ConsumerCardInsufficientBalanceException;
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
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @NotNull
    @Column( name = "number", nullable = false, unique = true )
    private Long number;

    @PositiveOrZero
    @Column( name = "balance", scale = 2 )
    private BigDecimal balance;

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
        final BigDecimal credits )
    {
        validateBalance( credits );
        this.balance = balance.add( credits );
    }

    private void validateBalance(
        final BigDecimal credits )
    {
        if( credits == null || credits.signum() == - 1 ) {
            throw new ConsumerCardInvalidCreditValueException( id, consumer.getId() );
        }
    }

    public void setBalance(
        final BigDecimal balance )
    {
        validateBalance( balance );
        this.balance = balance;
    }

    public void debit(
        final BigDecimal debitValue )
    {
        validateSufficientBalance( debitValue );
        this.balance = this.balance.subtract( debitValue );
    }

    private void validateSufficientBalance(
        final BigDecimal totalDebit )
    {
        if( balance.compareTo( totalDebit ) < 0 ) {
            throw new ConsumerCardInsufficientBalanceException( balance, totalDebit );
        }
    }
}
