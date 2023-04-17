package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.ValidDTO;

import javax.validation.Valid;


// 유효성 실패, 잘못된 파라메터 요청
// key : value를 넘겨준다.
// "username", "username의 길이가 초과 되었습니다"
@Getter
public class Exception400 extends RuntimeException {
    private String key;
    private String value;

    public Exception400(String key, String value) {
        super(value);
        this.key = key;
        this.value = value;
    }

    public ResponseDTO<?> body(){
        ResponseDTO<ValidDTO> responseDTO = new ResponseDTO<>();
        ValidDTO validDTO = new ValidDTO(key, value);

        responseDTO.fail(HttpStatus.BAD_REQUEST,"badRequest", validDTO);
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}