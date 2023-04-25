package shop.minostreet.shoppingmall.handler.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class MyValidationException extends RuntimeException{
    private Map<String, String> erroMap;

    public MyValidationException(String message, Map<String, String> erroMap) {
        super(message);
        this.erroMap = erroMap;
    }
}
