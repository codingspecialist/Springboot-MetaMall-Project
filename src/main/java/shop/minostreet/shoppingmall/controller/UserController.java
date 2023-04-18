package shop.minostreet.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.dto.user.UserReqDto;
import shop.minostreet.shoppingmall.dto.user.UserRespDto;
import shop.minostreet.shoppingmall.repository.LoginLogRepository;
import shop.minostreet.shoppingmall.repository.UserRepository;
import shop.minostreet.shoppingmall.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final LoginLogRepository loginLogRepository;
//    private final HttpSession session;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody JoinDto loginDto, HttpServletRequest request) {
//        Optional<User> userOP = userRepository.findByUsername(loginDto.getUsername());
//        if (userOP.isPresent()) {
//            // 1. 유저 정보 꺼내기
//            User loginUser = userOP.get();
//
//            // 2. 패스워드 검증하기
//            if(!loginUser.getPassword().equals(loginDto.getPassword())){
//                throw new Exception401("인증되지 않았습니다");
//            }
//
//            // 3. JWT 생성하기
//            String jwt = JwtProvider.create(userOP.get());
//
//            // 4. 최종 로그인 날짜 기록 (더티체킹 - update 쿼리 발생)
//            loginUser.setUpdatedAt(LocalDateTime.now());
//
//            // 5. 로그 테이블 기록
//            LoginLog loginLog = LoginLog.builder()
//                    .userId(loginUser.getId())
//                    .userAgent(request.getHeader("User-Agent"))
//                    .clientIP(request.getRemoteAddr())
//                    .build();
//            loginLogRepository.save(loginLog);
//
//            // 6. 응답 DTO 생성
//            ResponseDto<?> responseDto = new ResponseDto<>().data(loginUser);
//            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(responseDto);
//        } else {
//            throw new Exception400("유저네임 혹은 아이디가 잘못되었습니다");
//        }
//    }


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserReqDto.JoinReqDto joinReqDto, BindingResult bindingResult){    //유효성 검사
//    public void join(UserReqDto.JoinReqDto){    //기본전략이 x-www-urlencoded
//    public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto){    //JSON
        //담긴 에러를 처리
        //: AOP로 대체
//        if(bindingResult.hasErrors()){
//            //Map으로 담는다
//            Map<String, String> errorMap  = new HashMap<>();
//            for (FieldError error:bindingResult.getFieldErrors()) {
//                errorMap.put(error.getField(), error.getDefaultMessage());
//            }
//            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
//        }

        UserRespDto.JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 완료", joinRespDto), HttpStatus.CREATED);
    }

}
