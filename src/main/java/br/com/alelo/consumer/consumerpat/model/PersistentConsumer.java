package br.com.alelo.consumer.consumerpat.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "document_number", nullable = false )
    private String documentNumber;

    @Column( name = "birth_day" )
    private LocalDate birthday;

    @Embedded
    private ConsumerContact contact;

    @Embedded
    private ConsumerAddress address;
}