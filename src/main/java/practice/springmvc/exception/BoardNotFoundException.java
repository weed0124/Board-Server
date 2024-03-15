package practice.springmvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() {
        super("존재하지 않는 게시글입니다.");
    }
    public BoardNotFoundException(String message) {
        super(message);
    }
}
