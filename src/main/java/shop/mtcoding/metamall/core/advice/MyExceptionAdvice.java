package shop.mtcoding.metamall.core.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.metamall.core.exception.*;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.model.log.err.ErrorLog;
import shop.mtcoding.metamall.model.log.err.ErrorLogRepository;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MyExceptionAdvice {

    private final ErrorLogRepository errorLogRepository;

    private final HttpSession session;

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e) { // 잘못된 요청을 했을 때
        LoginUser loginUser = (LoginUser) session.getAttribute("login");
        if (loginUser != null) { //로그인이 되었을 때
            ErrorLog errorLog = ErrorLog.builder().userId(loginUser.getId()).msg("code :  400 - " + e.getMessage()).build();
            errorLogRepository.save(errorLog);
        }
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e) { // 인증되지 않았을 때
        LoginUser loginUser = (LoginUser) session.getAttribute("login");
        if (loginUser != null) { //로그인이 되었을 때
            ErrorLog errorLog = ErrorLog.builder().userId(loginUser.getId()).msg("code :  401 - " + e.getMessage()).build();
            errorLogRepository.save(errorLog);
        }
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e) { // 사용자가 잘못된 접근을 했을 때
        LoginUser loginUser = (LoginUser) session.getAttribute("login");
        System.out.println("에러 발생 유저 : " + loginUser);
        if (loginUser != null) { //로그인이 되었을 때
            ErrorLog errorLog = ErrorLog.builder().userId(loginUser.getId()).msg("code :  403 - " + e.getMessage()).build();
            errorLogRepository.save(errorLog);
        }
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> notFound(Exception404 e) { // 객체를 찾을 수 없을 때
        LoginUser loginUser = (LoginUser) session.getAttribute("login");
        if (loginUser != null) { //로그인이 되었을 때
            ErrorLog errorLog = ErrorLog.builder().userId(loginUser.getId()).msg("code :  404 - " + e.getMessage()).build();
            errorLogRepository.save(errorLog);
        }
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> serverError(Exception500 e) {
        LoginUser loginUser = (LoginUser) session.getAttribute("login");
        if (loginUser != null) { //로그인이 되었을 때
            ErrorLog errorLog = ErrorLog.builder().userId(loginUser.getId()).msg("code :  500 - " + e.getMessage()).build();
            errorLogRepository.save(errorLog);
        }
        return new ResponseEntity<>(e.body(), e.status());
    }
}