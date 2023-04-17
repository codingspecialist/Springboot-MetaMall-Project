package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
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
    // seller인지 아닌지는 할 필요 없다. -> 인터셉터가 판매자인지 권한 체크를 하니까
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

    /**
     * 상품 등록
     */
    // http://localhost:8080/products?page=1
    @GetMapping("/products")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable){
        // 1. 상품 찾기
        Page<Product> productPagePS = productRepository.findAll(pageable);

        // 2. 응답하기
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPagePS);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 상품목록보기
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        // 1. 상품 찾기
        Product productPS = productRepository.findById(id).orElseThrow(()->
                new Exception400("id", "해당 상품을 찾을 수 없습니다"));

        // 2. 응답하기
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 상품상세보기
     */

    /**
     * 상품수정하기
     */

    /**
     * 상품삭제하기
     */
}
