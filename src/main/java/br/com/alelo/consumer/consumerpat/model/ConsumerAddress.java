package br.com.alelo.consumer.consumerpat.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsumerAddress
{
    private String street;
    private Integer number;
    private String city;
    private String country;
    private String postalCode;
}
