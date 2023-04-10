package shop.mtcoding.metamall.handler.exception;


//커스텀 예외클래스 작성
public class MyApiException extends RuntimeException{
    public MyApiException(String message) {
        super(message);
    }
}
