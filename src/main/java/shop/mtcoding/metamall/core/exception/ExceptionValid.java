package shop.mtcoding.metamall.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.dto.ValidDto;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.util.MyConvertUtils;

import java.util.Map;

@Getter
public class ExceptionValid extends RuntimeException{
    private Map<String, String> erroMap;

    public ExceptionValid(Map<String, String> erroMap) {
        this.erroMap = erroMap;
    }

    public ResponseDto<?> body(){
        ResponseDto<ValidDto> responseDto = new ResponseDto<>();
        ValidDto errorDto = MyConvertUtils.hashToErrorDto(erroMap);
        responseDto.fail(HttpStatus.BAD_REQUEST, "validFail", errorDto);
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}
