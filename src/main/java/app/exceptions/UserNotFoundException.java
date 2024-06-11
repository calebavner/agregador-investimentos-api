package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

public class UserNotFoundException extends AppException {

    private UUID userId;

    public UserNotFoundException(UUID userId) {
        this.userId = userId;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pb.setTitle("User not found");
        pb.setDetail("There not user for id: " + userId);
        return pb;
    }
}
