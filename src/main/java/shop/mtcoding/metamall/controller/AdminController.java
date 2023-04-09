package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final UserRepository userRepository;

    @Transactional // 트랜잭션이 시작되지 않으면 강제로 em.flush() 를 할 수 없고, 더티체킹도 할 수 없다.
    @PutMapping("/admin/user/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody UserRequest.RoleUpdateDto roleUpdateDto) {
        User userPS = userRepository.findById(id)
                .orElseThrow(()-> new Exception400("잘못된 요청입니다"));
        userPS.updateRole(roleUpdateDto.getRole());

        ResponseDto<?> responseDto = new ResponseDto<>();
        return ResponseEntity.ok().body(responseDto);
    }
}
