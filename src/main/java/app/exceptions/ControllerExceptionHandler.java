package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ProblemDetail handlePersonalException(AppException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<InvalidFields> invalidFields = e.getFieldErrors()
                .stream()
                .map(f -> new InvalidFields(f.getField(), f.getDefaultMessage()))
                .toList();

        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Request parameters didn't validate");
        pb.setProperty("invalid fields", invalidFields);

        return pb;
    }

    private record InvalidFields(String field, String reason){}
}
