package shop.minostreet.shoppingmall.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;
import shop.minostreet.shoppingmall.handler.exception.MyForbiddenException;
import shop.minostreet.shoppingmall.handler.exception.MyValidationException;

@RestControllerAdvice
public class MyExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MyApiException.class)
    public ResponseEntity<?> apiException(MyApiException e){

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyValidationException.class)
    public ResponseEntity<?> validationApiException(MyValidationException e){

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErroMap()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MyForbiddenException.class)
    public ResponseEntity<?> forbiddenException(MyForbiddenException e){

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> Exception(Exception e){

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
