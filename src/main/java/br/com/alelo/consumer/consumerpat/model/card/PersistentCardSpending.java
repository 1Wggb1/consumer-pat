package br.com.alelo.consumer.consumerpat.model.card;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table( name = "card_spending" )
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class PersistentCardSpending
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @NotNull
    @Column( name = "establishment_name", nullable = false )
    private String establishmentName;

    @NotNull
    @Column( name = "establishment_type", nullable = false )
    @Enumerated( EnumType.STRING )
    private CardEstablishmentType establishmentType;

    @NotNull
    @Column( name = "purchase_date_time", nullable = false )
    private LocalDateTime purchaseDateTime;

    @NotEmpty
    @OneToMany( mappedBy = "cardSpending", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<PersistentCardSpendingProduct> products = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn( name = "consumer_card_id", nullable = false )
    private PersistentConsumerCard consumerCard;

    @NotNull
    @Column( name = "amount_cents", nullable = false )
    private Long amountCents;

    public PersistentCardSpending(
        final String establishmentName,
        final CardEstablishmentType establishmentType,
        final PersistentConsumerCard consumerCard,
        final Long amountCents )
    {
        this.establishmentName = establishmentName;
        this.establishmentType = establishmentType;
        this.purchaseDateTime = LocalDateTime.now();
        this.consumerCard = consumerCard;
        this.amountCents = amountCents;
    }
}
