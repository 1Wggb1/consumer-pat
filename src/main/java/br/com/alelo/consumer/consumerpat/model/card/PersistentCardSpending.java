package br.com.alelo.consumer.consumerpat.model.card;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column( name = "establishment_type" )
    @Enumerated( EnumType.STRING )
    private CardEstablishmentType establishmentType;

    @Column( name = "purchase_date_time", nullable = false )
    private LocalDateTime purchaseDateTime;

    @OneToMany( mappedBy = "cardSpending", cascade = CascadeType.PERSIST )
    private List<PersistentCardSpendingProduct> products = new ArrayList<>();

    @ManyToOne
    @JoinColumn( name = "consumer_card_id", nullable = false )
    private PersistentConsumerCard consumerCard;

    @Column( name = "amount_cents", nullable = false )
    private Long amountCents;

    public PersistentCardSpending(
        final String establishmentName,
        final List<PersistentCardSpendingProduct> products,
        final PersistentConsumerCard consumerCard,
        final Long amountCents )
    {
        this.establishmentName = establishmentName;
        this.products = products;
        this.purchaseDateTime = LocalDateTime.now();
        this.consumerCard = consumerCard;
        this.amountCents = amountCents;
    }
}
