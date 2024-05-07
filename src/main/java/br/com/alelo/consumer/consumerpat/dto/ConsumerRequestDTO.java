package br.com.alelo.consumer.consumerpat.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;
import br.com.alelo.consumer.consumerpat.model.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.ConsumerContact;

public record ConsumerRequestDTO(
    @NotEmpty( message = "Field name cannot be empty or null." ) String name,
    @NotEmpty( message = "Field documentNumber cannot be empty or null." ) @CPF(
        message = "Field documentNumber should be a valid CPF." ) String documentNumber,
    String birthday,
    @Valid @NotNull( message = "Field contact cannot be null." ) ConsumerContact contact,
    @Valid @NotNull( message = "Field address cannot be null." ) ConsumerAddress address )
{
}