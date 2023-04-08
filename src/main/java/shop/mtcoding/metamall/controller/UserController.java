package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.anotation.Authorize;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
    private final OrderSheetRepository orderSheetRepository;

    private final HttpSession session;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDto loginDto, HttpServletRequest request) {
        session.setAttribute("login", null);
        Optional<User> userOP = userRepository.findByUsername(loginDto.getUsername());
        if (userOP.isPresent()) {
            // 1. 유저 정보 꺼내기
            User user = userOP.get();

            // 2. 패스워드 검증하기
            if(!user.getPassword().equals(loginDto.getPassword())){
                throw new Exception401("인증되지 않았습니다");
            }

            // 3. JWT 생성하기
            String jwt = JwtProvider.create(userOP.get());

            // 4. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
            user.setUpdatedAt(LocalDateTime.now());

            // 5. 로그 테이블 기록
            LoginLog loginLog = LoginLog.builder()
                    .userId(user.getId())
                    .userAgent(request.getHeader("User-Agent"))
                    .clientIP(request.getRemoteAddr())
                    .build();
            loginLogRepository.save(loginLog);

//             6.  loginUser에 저장 - 에러에 사용하기 위해서
            LoginUser loginUser = LoginUser.builder().id(user.getId()).role(user.getRole().toString()).build();
            session.setAttribute("login", loginUser);
            System.out.println("로그인 후 유저 정보 : " + loginUser);

            // 7. 응답 DTO 생성
            ResponseDto<?> responseDto = new ResponseDto<>().data(user);
            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);
        } else {
            throw new Exception400("유저네임 혹은 아이디가 잘못되었습니다");
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody User joinUser){
        joinUser.setRole(Role.USER);
        userRepository.save(joinUser);
        OrderSheet orderSheet = OrderSheet.builder().user(joinUser).totalPrice(0).build();
        orderSheetRepository.save(orderSheet); // 한 고객 당 주문 시트 생성

        ResponseDto<?> responseDto = new ResponseDto<>().data(joinUser);
        return ResponseEntity.ok().body(responseDto);
    }
}
