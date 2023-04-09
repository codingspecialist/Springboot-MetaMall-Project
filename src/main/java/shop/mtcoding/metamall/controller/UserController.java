package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.annotation.MySameUserIdCheck;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
    private final HttpSession session;

    @MySameUserIdCheck
    @GetMapping("/users/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        User userPS =userRepository.findById(id).orElseThrow(
                ()-> new Exception400("id", "유저를 찾을 수 없습니다")
        );
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinDTO joinDTO, Errors errors) {
        User userPS = userRepository.save(joinDTO.toEntity());
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO loginDTO, Errors errors, HttpServletRequest request) {
        User sessionUser = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(
                        () -> new Exception400("username", "유저네임을 찾을 수 없습니다")
                );

        // 1. 패스워드 검증하기
        if (!sessionUser.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception400("password", "패스워드가 잘못입력되었습니다");
        }

        // 2. JWT 생성하기
        String jwt = JwtProvider.create(sessionUser);

        // 3. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
        sessionUser.setUpdatedAt(LocalDateTime.now());

        // 4. 로그 테이블 기록
        LoginLog loginLog = LoginLog.builder()
                .userId(sessionUser.getId())
                .userAgent(request.getHeader("User-Agent"))
                .clientIP(request.getRemoteAddr())
                .build();
        loginLogRepository.save(loginLog);

        // 5. 응답 DTO 생성
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(sessionUser);
        return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);
    }
}
