package br.com.alelo.consumer.consumerpat.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ConsumerAddress
{
    @NotEmpty( message = "Street cannot be null or empty." )
    private String street;
    @NotEmpty( message = "Number cannot be null or empty." )
    private String number;
    @NotEmpty( message = "City cannot be null or empty." )
    private String city;
    @NotEmpty( message = "Country cannot be null or empty." )
    private String country;
    private String postalCode;
}
