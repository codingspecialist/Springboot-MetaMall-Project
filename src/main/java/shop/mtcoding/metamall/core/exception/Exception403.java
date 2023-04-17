package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDTO;


// 권한 없음
@Getter
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<String> ResponseDto = new ResponseDTO<>();
        ResponseDto.fail(HttpStatus.FORBIDDEN, "forbidden", getMessage());
        return ResponseDto;
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}