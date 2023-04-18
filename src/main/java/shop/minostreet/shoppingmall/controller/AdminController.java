package shop.minostreet.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.domain.UserEnum;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.dto.user.UserReqDto;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;
import shop.minostreet.shoppingmall.repository.UserRepository;

import javax.validation.Valid;

/**
 * 권한 변경
 */
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AdminController {
    private final UserRepository userRepository;

    @Transactional // 트랜잭션이 시작되지 않으면 강제로 em.flush() 를 할 수 없고, 더티체킹도 할 수 없다. (원래는 서비스에서)
    @PutMapping("/admin/user/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody @Valid UserReqDto.RoleUpdateReqDto roleUpdateReqDto, Errors errors) {

        User userPS = userRepository.findById(id)
                .orElseThrow(() -> new MyApiException("해당 유저를 찾을 수 없습니다"));
        userPS.updateRole(roleUpdateReqDto.toEntity().getRole());

//        ResponseDTO<?> responseDto = new ResponseDTO<>();
//        return ResponseEntity.ok().body(responseDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원 권한 변경 완료", null), HttpStatus.OK);
    }
}
