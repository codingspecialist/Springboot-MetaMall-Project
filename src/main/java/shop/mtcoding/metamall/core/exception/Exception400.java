package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.ValidDto;

// username = fgkjdfogijerlgkj
// "username" :  "유저네임이 너무 길어요."

// 유효성 실패(잘못된 메서드 요청), 잘못된 파라메터 요청(Pathvariable)
@Getter
public class Exception400 extends RuntimeException {

    private String key;
    private String value;

    public Exception400(String key, String value) {
        super(value);
        this.key = key;
        this.value = value;
    }

    public ResponseDto<?> body(){
        ResponseDto<ValidDto> responseDto = new ResponseDto<>();
        ValidDto validDto = new ValidDto(key, value);
        responseDto.fail(HttpStatus.BAD_REQUEST, "badRequest", validDto);
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}