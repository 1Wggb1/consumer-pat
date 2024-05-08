package br.com.alelo.consumer.consumerpat.model.card;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    @Column( name = "unitary_price", nullable = false, scale = 2 )
    private BigDecimal unitaryPrice;

    @ManyToOne
    @JoinColumn( name = "card_spending_id", nullable = false )
    private PersistentCardSpending cardSpending;

    public PersistentCardSpendingProduct(
        final String productName,
        final Long quantity,
        final BigDecimal unitaryPrice,
        final PersistentCardSpending cardSpending )
    {
        this.productName = productName;
        this.quantity = quantity;
        this.unitaryPrice = unitaryPrice;
        this.cardSpending = cardSpending;
    }
}
