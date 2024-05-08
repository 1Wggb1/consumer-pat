package br.com.alelo.consumer.consumerpat.service;

import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_ADDRESS;
import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_CONTACT;

import java.time.LocalDate;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.model.consumer.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.consumer.ConsumerRepository;

@ExtendWith( SpringExtension.class )
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTestConfiguration
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ConsumerRepository consumerRepository;

    protected String writeAsJson(
        final Object object )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.writeValueAsString( object );
    }

    protected <T> T readAsObject(
        final String json,
        final Class<T> clazz )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue( json, clazz );
    }

    protected PersistentConsumer createConsumer(
        final String name,
        final String documentNumber )
    {
        final PersistentConsumer consumer = PersistentConsumer.builder()
            .name( name )
            .documentNumber( documentNumber )
            .birthday( LocalDate.of( 1999, 4, 17 ) )
            .address( CONSUMER_ADDRESS )
            .contact( CONSUMER_CONTACT )
            .build();
        return consumerRepository.save( consumer );
    }

    protected void validateErrorResponse(
        final ResultActions result,
        final ResultMatcher statusMatcher )
        throws Exception
    {
        result.andExpect( statusMatcher )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.status", IsNull.notNullValue() ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.dateTime", IsNull.notNullValue() ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages", IsNull.notNullValue() ) );
    }

    protected static void validatePageableResult(
        final ResultActions result,
        final PageableDTO expectedResult )
        throws Exception
    {
        final String pageableFields = "$.pageable.";
        final int numberOfElements = expectedResult.numberOfElements();
        result.andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "page", Is.is( expectedResult.page() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "size", Is.is( expectedResult.size() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "numberOfElements", Is.is( numberOfElements ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "totalPages", Is.is( expectedResult.totalPages() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "totalElements",
                Is.is( expectedResult.totalElements() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.elements.length()", Is.is( numberOfElements ) ) );
    }
}
