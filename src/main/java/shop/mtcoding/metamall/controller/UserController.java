package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
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

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinDto joinDto, BindingResult bindingResult) {
        User userPS = userRepository.save(joinDto.toEntity());
        ResponseDto<?> responseDto = new ResponseDto<>().data(userPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        User sessionUser = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(
                        () -> new Exception400("유저네임을 찾을 수 없습니다")
                );

        // 1. 패스워드 검증하기
        if (!sessionUser.getPassword().equals(loginDto.getPassword())) {
            throw new Exception400("패스워드가 잘못입력되었습니다");
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
        ResponseDto<?> responseDto = new ResponseDto<>().data(sessionUser);
        return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);

    }
}
