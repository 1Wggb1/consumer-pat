package br.com.alelo.consumer.consumerpat.model.card;

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
@Table( name = "card_product_spending" )
@NoArgsConstructor
@EqualsAndHashCode
public class PersistentCardSpendingProduct
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column( name = "name", nullable = false )
    private String productName;

    @Column( name = "quantity", nullable = false )
    private Long quantity;

    @Column( name = "unitary_price_cents", nullable = false )
    private Long unitaryPriceCents;

    @ManyToOne
    @JoinColumn( name = "card_spending_id", nullable = false )
    private PersistentCardSpending cardSpending;

    public PersistentCardSpendingProduct(
        final String productName,
        final Long quantity,
        final Long unitaryPriceCents,
        final PersistentCardSpending cardSpending )
    {
        this.productName = productName;
        this.quantity = quantity;
        this.unitaryPriceCents = unitaryPriceCents;
        this.cardSpending = cardSpending;
    }
}
