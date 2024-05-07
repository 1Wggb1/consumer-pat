package br.com.alelo.consumer.consumerpat.exception.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.alelo.consumer.consumerpat.dto.ErrorDTO;
import br.com.alelo.consumer.consumerpat.exception.ConsumerValidationException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class ConsumerExceptionHandler
{
    @ExceptionHandler( ConsumerValidationException.class )
    public ResponseEntity<ErrorDTO> handleValidationException(
        final ConsumerValidationException consumerValidationException )
    {
        final String message = consumerValidationException.getMessage();
        log.warn( "Consumer Validation Exception {}", message );
        return createErrorResponse( consumerValidationException.getStatus(), List.of( message ) );
    }

    private static ResponseEntity<ErrorDTO> createErrorResponse(
        final HttpStatus status,
        final List<String> errorMessages )
    {
        final ErrorDTO errorDTO = new ErrorDTO( status.toString(),
            errorMessages, LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) );
        return ResponseEntity.status( status ).body( errorDTO );
    }

    @ExceptionHandler( HttpMessageNotReadableException.class )
    public ResponseEntity<ErrorDTO> handleMessageNotReadableErrorException(
        final HttpMessageNotReadableException messageNotReadableException )
    {
        log.warn( "Not readable {}", messageNotReadableException.getMessage() );
        return createErrorResponse( HttpStatus.BAD_REQUEST, List.of( "Invalid payload syntax." ) );
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<ErrorDTO> handleMethodArgumentInvalidException(
        final MethodArgumentNotValidException argumentNotValidException )
    {
        log.warn( "Argument invalid error {}", argumentNotValidException.getMessage() );
        final List<FieldError> fieldErrors = argumentNotValidException.getFieldErrors();
        final List<String> errorMessages = new ArrayList<>();
        for( final FieldError fieldError : fieldErrors ) {
            errorMessages.add( fieldError.getDefaultMessage() );
        }
        return createErrorResponse( HttpStatus.BAD_REQUEST, errorMessages );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<ErrorDTO> handleInternalErrorException(
        final Exception exception )
    {
        log.error( "Internal error", exception );
        return createErrorResponse( HttpStatus.INTERNAL_SERVER_ERROR, List.of( "Internal error, contact support if the error persists." ) );
    }
}