package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import shop.mtcoding.metamall.core.annotation.MyErrorLogRecord;
import shop.mtcoding.metamall.core.exception.*;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.model.log.error.ErrorLogRepository;

@Slf4j
// @RequiredArgsConstructor
@RestControllerAdvice
public class MyExceptionAdvice {

    @MyErrorLogRecord
    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e){
        // trace -> debug -> info -> warn -> error
        log.debug("디버그 : " + e.getMessage());
        log.info("완료 : " + e.getMessage());
        log.warn("경고 : " + e.getMessage());
        log.error("에러 : " + e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e){
        log.debug("디버그 : " + e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e){
        log.debug("디버그 : " + e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> notFound(NoHandlerFoundException e){
        log.debug("디버그 : " + e.getMessage());
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(HttpStatus.NOT_FOUND, "notFound", e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    // 나머지 모든 예외는 이 친구에게 다 걸러진다.
    @MyErrorLogRecord
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverError(Exception e){
        log.debug("디버그 : " + e.getMessage());
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR, "unknownServerError", e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
