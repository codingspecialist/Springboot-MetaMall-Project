package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.log.err.ErrorLog;
import shop.mtcoding.metamall.model.log.err.ErrorLogRepository;


// 서버 에러
@Getter
public class Exception500 extends RuntimeException {

    public Exception500(String message) {
        super(message);
    }

    public ResponseDto<?> body(){
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR, "serverError", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}