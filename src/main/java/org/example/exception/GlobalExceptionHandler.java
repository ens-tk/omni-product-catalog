//package org.example.exception;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ProblemDetail;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.bind.support.WebExchangeBindException;
//
//import java.util.NoSuchElementException;
//import java.util.concurrent.TimeoutException;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(WebExchangeBindException.class)
//    public ProblemDetail handleValidation(WebExchangeBindException ex) {
//        String message = ex.getFieldErrors().stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .findFirst()
//                .orElse("Некорректные входные данные");
//
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.BAD_REQUEST, message);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ProblemDetail handleNotFound(NoSuchElementException ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.NOT_FOUND, ex.getMessage());
//    }
//
//    @ExceptionHandler(ProductTimeoutException.class)
//    public ProblemDetail handleProductTimeout(ProductTimeoutException ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.GATEWAY_TIMEOUT, ex.getMessage());
//    }
//
//    @ExceptionHandler(TimeoutException.class)
//    public ProblemDetail handleTimeout(TimeoutException ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.GATEWAY_TIMEOUT,
//                "Превышено время ожидания выполнения запроса");
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ProblemDetail handleConflict(DataIntegrityViolationException ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.CONFLICT,
//                "Нарушено ограничение целостности данных");
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ProblemDetail handleUnexpected(Exception ex) {
//        return ProblemDetail.forStatusAndDetail(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Внутренняя ошибка сервера");
//    }
//}