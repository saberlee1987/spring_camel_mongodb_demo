package com.saber.spring_camel_mongodb_demo.advicer;

import com.saber.spring_camel_mongodb_demo.dto.ErrorResponse;
import com.saber.spring_camel_mongodb_demo.dto.ServiceErrorResponseEnum;
import com.saber.spring_camel_mongodb_demo.dto.ValidationDto;
import com.saber.spring_camel_mongodb_demo.exceptions.BadRequestException;
import com.saber.spring_camel_mongodb_demo.exceptions.GatewayException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceDuplicationException;
import com.saber.spring_camel_mongodb_demo.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = GatewayException.class)
    public ResponseEntity<Object> handleGatewayException(GatewayException gatewayException) {
        int statusCode = gatewayException.getStatusCode();
        ErrorResponse errorResponse = gatewayException.getErrorResponse();

        log.error("Error for  correlation : {} , GatewayException with statusCode {} , body {} "
                , gatewayException.getCorrelation()
                , statusCode, errorResponse);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ServiceErrorResponseEnum.INPUT_VALIDATION_EXCEPTION.getCode());
        errorResponse.setMessage(ServiceErrorResponseEnum.INPUT_VALIDATION_EXCEPTION.getMessage());
        List<ValidationDto> validationDtoList = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            ValidationDto validationDto = new ValidationDto();
            validationDto.setFieldName(violation.getPropertyPath().toString());
            validationDto.setDetailMessage(violation.getMessage());
            validationDtoList.add(validationDto);
        }
        errorResponse.setValidations(validationDtoList);

        log.error("Error for handleConstraintViolationException with body ===> {}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ServiceErrorResponseEnum.INPUT_VALIDATION_EXCEPTION.getCode());
        errorResponse.setMessage(ServiceErrorResponseEnum.INPUT_VALIDATION_EXCEPTION.getMessage());
        List<ValidationDto> validationDtoList = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            ValidationDto validationDto = new ValidationDto();
            validationDto.setFieldName(fieldError.getField());
            validationDto.setDetailMessage(fieldError.getDefaultMessage());
            validationDtoList.add(validationDto);
        }
        errorResponse.setValidations(validationDtoList);

        log.error("Error for handleMethodArgumentNotValid with body ===> {}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ResourceDuplicationException.class)
    public ResponseEntity<?> resourceDuplicationException(ResourceDuplicationException exception) {
        log.error("ResourceDuplicationException error ====> {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}",
                HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        log.error("ResourceDuplicationException error ====> {}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception) {
        log.error("ResourceNotFoundException error ====> {}", exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorResponse.setMessage(HttpStatus.NOT_ACCEPTABLE.toString());
        errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"message\":\"%s\"}",
                HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage()));
        log.error("ResourceNotFoundException error ====> {}", errorResponse);

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException exception) {
        log.error("BadRequestException error ====> {}", exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(HttpStatus.BAD_REQUEST.toString());

        List<ValidationDto> validationDtoList = new ArrayList<>();

        ValidationDto validationDto = new ValidationDto();
        validationDto.setFieldName(exception.getFieldName());
        validationDto.setDetailMessage(exception.getDetailMessage());
        validationDtoList.add(validationDto);

        errorResponse.setValidations(validationDtoList);

        log.error("BadRequestException error ====> {}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}