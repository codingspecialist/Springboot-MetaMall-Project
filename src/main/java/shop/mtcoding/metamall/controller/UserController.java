package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.config.auth.PrincipalDetails;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;
import shop.mtcoding.metamall.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
    private final LoginService loginService;
    private final HttpSession session;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated @RequestBody UserRequest.JoinDto joinDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error: bindingResult.getFieldErrors())
                errorMap.put(error.getField(), error.getDefaultMessage());
            throw new Exception400("유효성검사가 실패했습니다.");

        } else {
            User user = joinDto.toEntity();
            User userEntity = loginService.join(user);
            return ResponseEntity.ok().body(userEntity);
        }
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserRequest.LoginDto loginDto,
                                   HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("나실행됨????????????????????????????????????????");
        Optional<User> userOP = userRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());

        if (userOP.isPresent()) {
            // 1. 유저 정보 꺼내기
            User loginUser = userOP.get();

            // 2. 패스워드 검증하기
            if (!loginUser.getPassword().equals(principalDetails.getUser().getPassword())) {
                throw new Exception401("인증되지 않았습니다");
            }

            // 3. JWT 생성하기
            String jwt = JwtProvider.create(userOP.get());
            System.out.println(jwt);
            // 4. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
            loginUser.setUpdatedAt(LocalDateTime.now());

            // 5. 로그 테이블 기록
            LoginLog loginLog = LoginLog.builder()
                    .userId(loginUser.getId())
                    .userAgent(request.getHeader("User-Agent"))
                    .clientIP(request.getRemoteAddr())
                    .build();
            loginLogRepository.save(loginLog);

            // 6. 응답 DTO 생성
            ResponseDto<?> responseDto = new ResponseDto<>().data(loginUser);
            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);

        } else {
            throw new Exception400("유저네임 혹은 아이디가 잘못되었습니다");
        }
    }
}
