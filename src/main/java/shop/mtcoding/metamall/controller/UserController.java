package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 회원가입, 로그인
 */
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
    private final HttpSession session;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDTO joinDTO) {
        if (joinDTO.getUsername().isBlank()) {
            throw new Exception400("username을 입력해주세요.");
        }
        if (joinDTO.getPassword().isBlank()) {
            throw new Exception400("password를 입력해주세요.");
        }
        if (joinDTO.getEmail().isBlank()) {
            throw new Exception400("email을 입력해주세요.");
        }
        if (joinDTO.getRole().isBlank()) {
            throw new Exception400("role을 입력해주세요.");
        }

        User userPS = userRepository.save(joinDTO.toEntity());

        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(userPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO loginDTO, HttpServletRequest request) {
        Optional<User> userOP = userRepository.findByUsername(loginDTO.getUsername());
        if (userOP.isPresent()) {
            // 1. 유저 정보 꺼내기
            User loginUser = userOP.get();

            // 2. 패스워드 검증하기
            if (!loginUser.getPassword().equals(loginDTO.getPassword())) {
                throw new Exception401("인증되지 않았습니다");
            }

            // 3. JWT 생성하기
            String jwt = JwtProvider.create(userOP.get());

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
            ResponseDTO<?> responseDto = new ResponseDTO<>().data(loginUser);
            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);
        } else {
            throw new Exception400("username 혹은 id가 잘못되었습니다");
        }
    }
}
