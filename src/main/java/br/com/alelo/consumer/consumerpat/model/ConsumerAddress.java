package br.com.alelo.consumer.consumerpat.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import br.com.alelo.consumer.consumerpat.util.UnmaskUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
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

    public ConsumerAddress(
        final String street,
        final String number,
        final String city,
        final String country,
        final String postalCode )
    {
        this.street = street;
        this.number = number;
        this.city = city;
        this.country = country;
        this.postalCode = UnmaskUtil.unmaskPostalCode( postalCode );
    }
}
