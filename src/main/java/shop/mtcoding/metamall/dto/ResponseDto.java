package shop.mtcoding.metamall.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.metamall.core.CodeEnum;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private Integer status; // 에러시에 의미 있음.
    private String msg; // 에러시에 의미 있음. ex) badRequest
    private T data; // 에러시에는 구체적인 에러 내용 ex) username이 입력되지 않았습니다

    public ResponseDto(){
        this.status = HttpStatus.OK.value();
        this.msg = "성공";
        this.data = null;
    }

    public ResponseDto<?> code(CodeEnum errorCode){
        this.status = errorCode.getCode();
        return this;
    }
    public ResponseDto<?> code(int errorCode){
        this.status = errorCode;
        return this;
    }
    public ResponseDto<?> data(T data){
        this.data = data; // 응답할 데이터 바디
        return this;
    }
    public ResponseDto<?> msg(String msg){
        this.msg = msg;
        return this;
    }

    public ResponseDto<?> fail(HttpStatus httpStatus, String msg, T data){
        this.status = httpStatus.value();
        this.msg = msg; // 에러 제목
        this.data = data; // 에러 내용
        return this;
    }


}
