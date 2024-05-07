package br.com.alelo.consumer.consumerpat.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsumerContact
{
    @NotNull( message = "Mobile Phone Number cannot be null." )
    private Long mobilePhoneNumber;
    private Long residencePhoneNumber;
    @NotEmpty( message = "Email cannot be null or empty." )
    @Email( message = "Email should be a valid format." )
    private String email;
}
