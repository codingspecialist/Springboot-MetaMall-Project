package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.core.annotation.MySessionStore;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 상품등록 상품목록보기 상품상세보기 상품수정하기 상품삭제하기
 */
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    /**
     * 상품 등록
     */
    @PostMapping("/seller/products") // 앞에 seller가 있다? -> interceptor가 권한 판단
    public ResponseEntity<?> save(@RequestBody @Valid ProductRequest.SaveDTO saveDTO, Errors errors, @MySessionStore SessionUser sessionUser) {
        // 1. 판매자 찾기
        User sellerPS = userRepository.findById(sessionUser.getId()).orElseThrow(() ->
            new Exception400("id", "판매자를 찾을 수 없습니다.")
        );

        // 2. 상품 등록하기
        Product productPS = productRepository.save(saveDTO.toEntity(sellerPS));
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
