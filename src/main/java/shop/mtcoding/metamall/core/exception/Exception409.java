package shop.mtcoding.metamall.core.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDto;


// 인증 안됨
@Getter
public class Exception409 extends RuntimeException {
    public Exception409(String message) {
        super(message);
    }

    public ResponseDto<?> body(){
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.fail(HttpStatus.CONFLICT, "conflict", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.CONFLICT;
    }
}