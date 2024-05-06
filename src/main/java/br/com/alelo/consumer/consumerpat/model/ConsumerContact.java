package br.com.alelo.consumer.consumerpat.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsumerContact
{
    private Long mobilePhoneNumber;
    private Long residencePhoneNumber;
    @Email
    private String email;
}
