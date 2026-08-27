package com.gft.prices.infrastructure.entrypoint.advice;

import com.gft.prices.domain.exception.PriceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldReturn404ProblemDetailWhenPriceIsNotFound() {

        LocalDateTime applicationDate =
                LocalDateTime.of(2020, 6, 14, 10, 0);

        PriceNotFoundException exception =
                new PriceNotFoundException(
                        1L,
                        99999L,
                        applicationDate);

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/prices");

        ProblemDetail problem =
                handler.handlePriceNotFound(
                        exception,
                        request);

        assertThat(problem.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());

        assertThat(problem.getTitle())
                .isEqualTo("Price not found");

        assertThat(problem.getDetail())
                .isEqualTo(exception.getMessage());

        assertThat(problem.getInstance())
                .isEqualTo(URI.create("/prices"));
    }

    @Test
    void shouldReturn400ProblemDetailWhenParameterTypeIsInvalid() {

        MethodParameter parameter = mock(MethodParameter.class);

        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "abc",
                        Long.class,
                        "productId",
                        parameter,
                        null);

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/prices");

        ProblemDetail problem =
                handler.handleTypeMismatch(
                        exception,
                        request);

        assertThat(problem.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());

        assertThat(problem.getTitle())
                .isEqualTo("Invalid request parameter");

        assertThat(problem.getDetail())
                .isEqualTo("Parameter 'productId' has invalid value 'abc'");

        assertThat(problem.getInstance())
                .isEqualTo(URI.create("/prices"));
    }

    @Test
    void shouldReturn400ProblemDetailWhenRequiredParameterIsMissing() {

        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException(
                        "applicationDate",
                        "String");

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/prices");

        ProblemDetail problem =
                handler.handleMissingParameter(
                        exception,
                        request);

        assertThat(problem.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());

        assertThat(problem.getTitle())
                .isEqualTo("Missing request parameter");

        assertThat(problem.getDetail())
                .isEqualTo("Required parameter 'applicationDate' is missing");

        assertThat(problem.getInstance())
                .isEqualTo(URI.create("/prices"));
    }
}
