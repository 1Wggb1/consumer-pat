package br.com.alelo.consumer.consumerpat.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "card_spending" )
@NoArgsConstructor
@EqualsAndHashCode
public class PersistentCardSpending
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Integer id;

    @Column( name = "establishment_name", nullable = false )
    private String establishmentName;

    @Column( name = "product_description", nullable = false )
    private String productDescription;

    @Column( name = "purchase_date_time", nullable = false )
    private LocalDateTime purchaseDateTime;

    @ManyToOne
    @JoinColumn( name = "consumer_card_id", nullable = false )
    private PersistentConsumerCard consumerCard;

    @Column( name = "amount", nullable = false )
    private Double amount;

    public PersistentCardSpending(
        final String establishmentName,
        final String productDescription,
        final Integer cardNumber,
        final Double amount )
    {
        this.establishmentName = establishmentName;
        this.productDescription = productDescription;
        this.purchaseDateTime = LocalDateTime.now();
        // this.cardNumber = cardNumber;
        this.amount = amount;
    }
}
