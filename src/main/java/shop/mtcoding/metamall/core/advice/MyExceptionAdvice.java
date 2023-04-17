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
@RequiredArgsConstructor
@RestControllerAdvice
public class MyExceptionAdvice {

    @MyErrorLogRecord
    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e) {
        // e.body() : ResponseDTO
        // trace -> debug -> info -> warn -> error
        log.debug("DEBUG : " + e.getMessage());
        log.info("INFO : " + e.getMessage());
        log.warn("WARN : " + e.getMessage());
        log.error("ERROR : " + e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @MyErrorLogRecord
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> notFound(NoHandlerFoundException e) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.NOT_FOUND, "notFound", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    // 나머지 모든 예외(알 수 없는 에러)
    @MyErrorLogRecord
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverError(NoHandlerFoundException e) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "unknownServerError", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
