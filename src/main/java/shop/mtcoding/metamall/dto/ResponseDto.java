package shop.mtcoding.metamall.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDTO<T> {
    private Integer status; // 에러시에 의미 있음.
    private String msg; // 에러시에 의미 있음. ex) badRequest
    private T data; // 에러시에는 구체적인 에러 내용 ex) username이 입력되지 않았습니다

    // delete하여 성공했을 경우만 사용한다.
    public ResponseDTO(){
        this.status = HttpStatus.OK.value();
        this.msg = "성공";
        this.data = null;
    }

    // get, post, put : 성공 시
    // get : 조회 시 data를 담아서 돌려준다.
    public ResponseDTO<?> data(T data){
        this.data = data; // 응답할 데이터 바디
        return this;
    }

    // 4가지 요청에 대한 실패 시
    public ResponseDTO<?> fail(HttpStatus httpStatus, String msg, T data){
        this.status = httpStatus.value();
        this.msg = msg; // 에러 제목
        this.data = data; // 에러 내용
        return this;
    }
}
