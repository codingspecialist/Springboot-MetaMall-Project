package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.ValidDTO;


// 유효성 실패(바디데이터), 잘못된 파라미터 요청(뭐가 잘못되었는지)
@Getter
public class Exception400 extends RuntimeException {

    private String key;
    private String value;
    public Exception400(String key,String value) {
        super(value);
        this.key=key;
        this.value=value;
    }

    public ResponseDTO<?> body(){
        ResponseDTO<ValidDTO> responseDto = new ResponseDTO<>();
        ValidDTO validDTO=new ValidDTO(key,value);
        responseDto.fail(HttpStatus.BAD_REQUEST, "badRequest", validDTO);
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}