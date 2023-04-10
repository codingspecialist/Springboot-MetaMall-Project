 package shop.mtcoding.metamall.handler.exception;


//커스텀 예외클래스 작성
public class MyForbiddenException extends RuntimeException{
    public MyForbiddenException(String message) {
        super(message);
    }
}
