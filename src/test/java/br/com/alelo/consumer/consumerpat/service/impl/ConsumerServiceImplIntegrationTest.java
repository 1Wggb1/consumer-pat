package br.com.alelo.consumer.consumerpat.service.impl;

import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_ADDRESS;
import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_CONTACT;
import static br.com.alelo.consumer.consumerpat.TestData.CONSUMER_DTO;
import static br.com.alelo.consumer.consumerpat.TestData.SECOND_VALID_DOCUMENT_NUMBER;
import static br.com.alelo.consumer.consumerpat.TestData.SECOND_VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static br.com.alelo.consumer.consumerpat.TestData.VALID_DOCUMENT_NUMBER_WITHOUT_MASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.alelo.consumer.consumerpat.dto.ConsumerRequestDTO;
import br.com.alelo.consumer.consumerpat.dto.ConsumerResponseDTO;
import br.com.alelo.consumer.consumerpat.dto.PageableDTO;
import br.com.alelo.consumer.consumerpat.model.ConsumerContact;
import br.com.alelo.consumer.consumerpat.model.PersistentConsumer;
import br.com.alelo.consumer.consumerpat.repository.ConsumerRepository;

@ExtendWith( SpringExtension.class )
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
class ConsumerServiceImplIntegrationTest
{
    private static final String BASE_PATH = "/v1/consumers";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ConsumerRequestDTO INVALID_CONSUMER_DTO = new ConsumerRequestDTO( null,
        null,
        null,
        null,
        null );

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ConsumerRepository consumerRepository;

    @AfterEach
    void tearDown()
    {
        consumerRepository.deleteAll();
    }

    @Test
    @DisplayName( "Deve retornar 201 quando consumidor criado com sucesso." )
    void shouldReturn201WhenConsumerCreatedSuccessfully()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/consumers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( CONSUMER_DTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isCreated() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.id", IsNull.notNullValue() ) );
        final String response = result.andReturn().getResponse().getContentAsString();
        final ConsumerResponseDTO responseDTO = readAsObject( response, ConsumerResponseDTO.class );
        final PersistentConsumer createdConsumer = consumerRepository.findById( responseDTO.id() )
            .orElseThrow();
        assertNotNull( createdConsumer.getId() );
        assertDTOAndPersistentFields( CONSUMER_DTO, createdConsumer );
    }

    private static void assertDTOAndPersistentFields(
        final ConsumerRequestDTO expectedConsumer,
        final PersistentConsumer persistentConsumer )
    {
        assertEquals( expectedConsumer.name(), persistentConsumer.getName() );
        assertEquals( LocalDate.parse( expectedConsumer.birthday(), DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ),
            persistentConsumer.getBirthday() );
        assertEquals( expectedConsumer.documentNumber().replaceAll( "[-.]", "" ),
            persistentConsumer.getDocumentNumber() );
        assertEquals( expectedConsumer.address(), persistentConsumer.getAddress() );
        assertEquals( expectedConsumer.contact(), persistentConsumer.getContact() );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando payload não está no formato correto." )
    void shouldReturnErrorMessageWhenInvalidPayloadFormat()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH )
            .contentType( MediaType.APPLICATION_JSON )
            .content( "{error: " );
        final ResultActions result = mvc.perform( request );

        validateErrorResponse( result, MockMvcResultMatchers.status().isBadRequest() );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando campos do payload estão inválidos na criação." )
    void shouldReturn400ErrorMessageWhenInvalidPayloadOnCreation()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( BASE_PATH );
        doInvalidPayloadRequest( request );
    }

    private void doInvalidPayloadRequest(
        final MockHttpServletRequestBuilder servletRequestBuilder )
        throws Exception
    {
        final MockHttpServletRequestBuilder request = servletRequestBuilder
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( INVALID_CONSUMER_DTO ) );
        final ResultActions result = mvc.perform( request );
        validateInvalidPayload( result );
    }

    private void validateInvalidPayload(
        final ResultActions result )
        throws Exception
    {
        final ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();
        validateErrorResponse( result, badRequest );
        result.andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", IsEqual.equalTo( 4 ) ) );
        assertTrue( consumerRepository.findAll().isEmpty() );
    }

    private static String writeAsJson(
        final Object object )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.writeValueAsString( object );
    }

    private static void validateErrorResponse(
        final ResultActions result,
        final ResultMatcher statusMatcher )
        throws Exception
    {
        result.andExpect( statusMatcher )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.status", IsNull.notNullValue() ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.dateTime", IsNull.notNullValue() ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages", IsNull.notNullValue() ) );
    }

    private static <T> T readAsObject(
        final String json,
        final Class<T> clazz )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue( json, clazz );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando campos do payload estão inválidos na atualização." )
    void shouldReturn400ErrorMessageWhenInvalidPayloadOnUpdate()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( BASE_PATH + "/1000" );
        doInvalidPayloadRequest( request );
    }

    @Test
    @DisplayName( "Deve retornar 204 quando consumidor atualizado com sucesso." )
    void shouldReturn204WhenConsumerUpdatedSuccessfully()
        throws Exception
    {
        final PersistentConsumer persistentConsumer = createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        final ConsumerContact contact = new ConsumerContact( 4578569L,
            859654L,
            "mar@gmail.ru" );
        final ConsumerRequestDTO consumerToUpdate = new ConsumerRequestDTO( "Marcella",
            SECOND_VALID_DOCUMENT_NUMBER,
            "2002-05-15",
            contact,
            CONSUMER_ADDRESS );
        final Integer consumerId = persistentConsumer.getId();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( BASE_PATH + "/{id}", consumerId )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( consumerToUpdate ) );

        final ResultActions result = mvc.perform( request );
        result
            .andExpect( MockMvcResultMatchers.status().isNoContent() );
        final PersistentConsumer updatedConsumer = consumerRepository.findById( consumerId )
            .orElseThrow();
        assertNotNull( updatedConsumer.getId() );
        assertDTOAndPersistentFields( consumerToUpdate, updatedConsumer );
    }

    private PersistentConsumer createConsumer(
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

    @Test
    @DisplayName( "Deve retornar 404 quando consumidor não encontrado na atualização." )
    void shouldReturn404WhenConsumerNotFoundOUpdate()
        throws Exception
    {
        final int id = 11;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( BASE_PATH + "/{id}", id )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( CONSUMER_DTO ) );

        final ResultActions result = mvc.perform( request );

        assertTrue( consumerRepository.findById( id ).isEmpty() );
        validateErrorResponse( result, MockMvcResultMatchers.status().isNotFound() );
    }

    @Test
    @DisplayName( "Deve retornar 200 e consumidores paginados." )
    void shouldReturn200AndConsumersByPagination()
        throws Exception
    {
        create3DefaultsConsumers();
        final int page = 0;
        final int size = 2;

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( BASE_PATH )
            .queryParam( "page", String.valueOf( page ) )
            .queryParam( "size", String.valueOf( size ) );

        final ResultActions result = mvc.perform( request );
        final PageableDTO expectedPageable = new PageableDTO( page, size, 2, 2, 3 );
        validatePageableResult( result, expectedPageable );
    }

    private void create3DefaultsConsumers()
    {
        createConsumer( "Will", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        createConsumer( "Marcella", SECOND_VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
        createConsumer( "Marcia", VALID_DOCUMENT_NUMBER_WITHOUT_MASK );
    }

    private static void validatePageableResult(
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

    @Test
    @DisplayName( "Deve retornar 200 quando consumidores não encontrados." )
    void shouldReturn200WithoutResultWhenThereArentConsumers()
        throws Exception
    {
        final int page = 0;
        final int size = 2;

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( BASE_PATH )
            .queryParam( "page", String.valueOf( page ) )
            .queryParam( "size", String.valueOf( size ) );

        final ResultActions result = mvc.perform( request );
        final PageableDTO expectedPageable = new PageableDTO( page, size,
            0, 0, 0 );
        validatePageableResult( result, expectedPageable );
    }
}