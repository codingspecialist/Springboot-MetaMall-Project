package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.core.annotation.PwdCk;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Validated
@Transactional
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid @PwdCk UserRequest.SignUpDto signUpDto){
        User signUpUser = userRepository.save(User.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .email(signUpDto.getEmail())
                .role(signUpDto.getRole())
                .build());
        return ResponseEntity.ok().body(new ResponseDto<>().data(signUpUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDto loginDto, HttpServletRequest request) {
        Optional<User> userOP = userRepository.findByUsername(loginDto.getUsername());
        if (userOP.isPresent()) {
            // 1. 유저 정보 꺼내기
            User loginUser = userOP.get();

            // 2. 패스워드 검증하기
            if(!loginUser.getPassword().equals(loginDto.getPassword())){
                throw new Exception401("인증되지 않았습니다");
            }

            // 3. JWT 생성하기
            String jwt = JwtProvider.create(userOP.get());

            // 4. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
            loginUser.updateLastLoginDate(LocalDateTime.now());

            // 5. 로그 테이블 기록
            //AOP로

            // 6. 응답 DTO 생성
            ResponseDto<?> responseDto = new ResponseDto<>().data(loginUser);
            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);
        } else {
            throw new Exception400("유저네임 혹은 아이디가 잘못되었습니다");
        }
    }
}
