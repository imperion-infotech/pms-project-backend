/**
 * 
 */
package com.pms.exception;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;

@RestControllerAdvice
public class GlobalProblemDetailHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalProblemDetailHandler.class);

    /**
     * Resource not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex) {

        ProblemDetail pd = buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                ex
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    /**
     * Business exception
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {

        ProblemDetail pd = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Business Error",
                ex.getMessage(),
                ex
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /**
     * Unauthorized exception
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorized(UnauthorizedException ex) {

        ProblemDetail pd = buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                ex.getMessage(),
                ex
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(pd);
    }

    /**
     * Validation error handling
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ProblemDetail pd = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "Validation failed for one or more fields",
                ex
        );

        pd.setProperty("errors", errors);
        pd.setProperty("errorCount", errors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    /**
     * Generic fallback exception handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {

        ProblemDetail pd = buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Unexpected error occurred",
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    /**
     * Reusable ProblemDetail builder
     */
    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            Exception ex) {

        String requestTraceId = RequestTraceContext.get();
        String businessTraceId = BusinessTraceContext.get();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);

        pd.setTitle(title);
        pd.setType(URI.create("https://api.pms.com/errors/" + status.value()));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("requestTraceId", requestTraceId);
        pd.setProperty("businessTraceId", businessTraceId);
        pd.setProperty("exception", ex.getClass().getSimpleName());

        if (status.is5xxServerError()) {
            log.error("[requestTraceId={}, businessTraceId={}] {}",
                    requestTraceId,
                    businessTraceId,
                    ex.getMessage(),
                    ex);
        } else {
            log.warn("[requestTraceId={}, businessTraceId={}] {}",
                    requestTraceId,
                    businessTraceId,
                    ex.getMessage());
        }

        return pd;
    }
}