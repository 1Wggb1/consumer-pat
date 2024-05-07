package br.com.alelo.consumer.consumerpat.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.alelo.consumer.consumerpat.util.UnmaskUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table( name = "consumer" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentConsumer
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Integer id;

    @NotEmpty
    @Column( name = "name", nullable = false )
    private String name;

    @NotEmpty
    @Column( name = "document_number", nullable = false )
    private String documentNumber;

    @Column( name = "birth_day" )
    private LocalDate birthday;

    @NotNull
    @Embedded
    private ConsumerContact contact;

    @NotNull
    @Embedded
    private ConsumerAddress address;

    public void setId(
        final Integer id )
    {
        throw new UnsupportedOperationException( "Id cannot be changed." );
    }

    public void setDocumentNumber(
        final String documentNumber )
    {
        this.documentNumber = UnmaskUtil.unmaskDocumentNumber( documentNumber );
    }
}