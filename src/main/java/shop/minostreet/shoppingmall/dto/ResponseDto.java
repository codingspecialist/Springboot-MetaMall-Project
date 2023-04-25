package shop.minostreet.shoppingmall.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; // 에러시에 의미 있음.
    private final String msg; // 에러시에 의미 있음. ex) badRequest
    private final T data; // 에러시에는 구체적인 에러 내용 ex) username이 입력되지 않았습니다

//    public ResponseDto(){
//        this.code = HttpStatus.OK.value();
//        this.msg = "성공";
//        this.data = null;
//    }
//
//    public ResponseDto<?> data(T data){
//        this.data = data; // 응답할 데이터 바디
//        return this;
//    }
//
//    public ResponseDto<?> fail(HttpStatus httpStatus, String msg, T data){
//        this.code = httpStatus.value();
//        this.msg = msg; // 에러 제목
//        this.data = data; // 에러 내용
//        return this;
//    }
}
