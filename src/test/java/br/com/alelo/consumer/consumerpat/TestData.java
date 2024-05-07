package br.com.alelo.consumer.consumerpat;

import br.com.alelo.consumer.consumerpat.dto.consumer.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.model.ConsumerAddress;
import br.com.alelo.consumer.consumerpat.model.ConsumerContact;

public interface TestData
{
    String VALID_DOCUMENT_NUMBER = "995.831.410-03";
    String VALID_DOCUMENT_NUMBER_WITHOUT_MASK = "99583141003";
    String SECOND_VALID_DOCUMENT_NUMBER = "794.868.610-05";
    String SECOND_VALID_DOCUMENT_NUMBER_WITHOUT_MASK = "79486861005";
    ConsumerContact CONSUMER_CONTACT = new ConsumerContact( 551789666666L,
        7789955566L,
        "joao@email.com" );
    ConsumerAddress CONSUMER_ADDRESS = new ConsumerAddress( "Rua São Angelo", "98-a",
        "São Paulo", "BRA", "06766140" );
    ConsumerRequestDTO CONSUMER_DTO = new ConsumerRequestDTO( "João",
        VALID_DOCUMENT_NUMBER,
        "1999-04-17",
        CONSUMER_CONTACT,
        CONSUMER_ADDRESS );
}
