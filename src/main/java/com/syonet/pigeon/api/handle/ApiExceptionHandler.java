package com.syonet.pigeon.api.handle;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.syonet.pigeon.domain.exception.BusinessException;
import com.syonet.pigeon.domain.exception.RecordNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERROR_GENERIC_FINAL = "An unexpected internal system error has occurred. " +
            "Try again and if the problem persists contact your system administrator.";

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();
        ExceptionDetails details = createExceptionBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleNegocioException(BusinessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.BUSINESS_ERROR;
        String detail = ex.getMessage();
        ExceptionDetails details = createExceptionBuilder(status, type, detail)
                .userMessage(ex.getLocalizedMessage())
                .build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.INVALID_DATA;
        String detail = String.format("Parameter '%s' did not fulfill the validation required for the transaction.",
                ex.toString());
        ExceptionDetails details = createExceptionBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.INVALID_DATA;
        String detail = ex.getMessage();
        ExceptionDetails details = createExceptionBuilder(status, type, detail).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ExceptionType exceptionType = ExceptionType.INCOMPREHENSIBLE_MESSAGE;
        String details = String.format("The property '%s' received the value '%s', which is of an invalid type. " +
                        "Correct it and enter a value compatible with the %s type.", path,
                ex.getValue(), ex.getTargetType().getSimpleName());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, exceptionType, details)
                .userMessage(MSG_ERROR_GENERIC_FINAL)
                .build();

        return handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBidingException(PropertyBindingException ex, HttpHeaders headers,
                                                                 HttpStatusCode statusCode, WebRequest request){
        ExceptionType exceptionType = ExceptionType.INCOMPREHENSIBLE_MESSAGE;
        String path = joinPath(ex.getPath());
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String details = "";
        if(rootCause instanceof UnrecognizedPropertyException) {
            details = String.format("The property '%s' is not a parameter compatible with the request expected by the api, " +
                    "remove the property and try again.", ex.getPropertyName());
        } else if (rootCause instanceof IgnoredPropertyException){
            details = String.format("The property '%s' does not exist, remove or correct the property and try again.",
                    ex.getPropertyName());
        }
        ExceptionDetails exceptionDetails = createExceptionBuilder(statusCode, exceptionType, details)
                .userMessage(MSG_ERROR_GENERIC_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.INVALID_DATA;
        String details = String.format("One or more fields are invalid. Please fill them in correctly and try again.");
        BindingResult bindingResult = ex.getBindingResult();
        List<ExceptionDetails.Field> exceptionFields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> ExceptionDetails.Field.builder()
                        .name(fieldError.getField())
                        .userMessage(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(details)
                .fields(exceptionFields)
                .build();

        return super.handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        if(ex instanceof MethodArgumentTypeMismatchException){
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException)ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException (MethodArgumentTypeMismatchException ex, HttpHeaders headers,
                                                                              HttpStatusCode statusCode, WebRequest request) {
        ExceptionType exceptionType = ExceptionType.INVALID_PARAMETER;
        String details = String.format("The URL parameter '%s' received the value '%s', which is of an invalid type. " +
                "Correct and enter a value compatible with the '%s' type.", ex.getParameter().getParameterName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        ExceptionDetails exceptionDetails = createExceptionBuilder(statusCode, exceptionType, details)
                .userMessage(MSG_ERROR_GENERIC_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, headers, statusCode, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionType type = ExceptionType.SYSTEM_ERROR;
        String detail = MSG_ERROR_GENERIC_FINAL;

        ex.printStackTrace();

        ExceptionDetails details = createExceptionBuilder(status, type, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, details, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String details = String.format("The Resource '%s', which you tried to access, is non-existent", ex.getRequestURL());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(MSG_ERROR_GENERIC_FINAL)
                .build();
        return this.handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        String details = String.format("The Resource '%s', which you tried to access, is non-existent", ex.getResourcePath());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(MSG_ERROR_GENERIC_FINAL)
                .build();
        return this.handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if(body == null){
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .title(statusCode.toString())
                    .timestamp(LocalDateTime.now())
                    .detail(ex.getMessage())
                    .userMessage(MSG_ERROR_GENERIC_FINAL)
                    .build();
        } else if (body instanceof String) {
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .title(statusCode.toString())
                    .timestamp(LocalDateTime.now())
                    .detail(ex.getMessage())
                    .userMessage(MSG_ERROR_GENERIC_FINAL)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ExceptionDetails.ExceptionDetailsBuilder createExceptionBuilder(HttpStatusCode status, ExceptionType type,
                                                                            String detail){
        return ExceptionDetails.builder()
                .status(status.value())
                .title(status.toString())
                .type(type.getUri())
                .detail(detail)
                .timestamp(LocalDateTime.now());
    }

    private String joinPath(List<JsonMappingException.Reference> references){
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

}
