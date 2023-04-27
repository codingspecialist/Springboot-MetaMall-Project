package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.annotation.MySessionStore;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 상품등록, 상품목록보기, 상품상세보기, 상품수정하기, 상품삭제하기
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpSession session;

    // 인터셉터가 판매자인지 권한체크를 할 것이니까!!
    @PostMapping("/seller/products")
    public ResponseEntity<?> save(
            @RequestBody @Valid ProductRequest.SaveDTO saveDTO, Errors errors,
            @MySessionStore SessionUser sessionUser) {

        // 1. 인증

        // 2. 판매자 권한

        // 3. 유효성 검사 통과
//        if (errors.hasErrors()) {
//            throw new
//        }

        // 4. 판매자가 존재 여부 확인

        // 5. 상품 등록

        // 6. 응답


        // 1. 판매자 찾기
        User sellerPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(
                        () -> new Exception400("id", "판매자를 찾을 수 없습니다.")
                );

        // 2. 상품 등록하기
        Product productPS = productRepository.save(saveDTO.toEntity(sellerPS));
        ResponseDto<?> responseDto = new ResponseDto<>().data(productPS);
        return ResponseEntity.ok(responseDto);
    }

    // http://localhost:8080/products?page=1
    @GetMapping("/products")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        // 1. 상품 찾기
        Page<Product> productPagePS = productRepository.findAll(pageable);

        // 2. 응답하기
        ResponseDto<?> responseDto = new ResponseDto<>().data(productPagePS);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        // 1. 상품 찾기
        Product productPS = productRepository.findById(id).orElseThrow(() -> {
            throw new Exception400("id", "해당 상품을 찾을 수 없습니다.");
        });

        // 2. 응답하기
        ResponseDto<?> responseDto = new ResponseDto<>().data(productPS);
        return ResponseEntity.ok(responseDto);
    }

    @Transactional // 더티체킹 하고 싶다면 붙이기!!
    @PutMapping("/seller/products/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid ProductRequest.UpdateDTO updateDTO,
                                    Errors errors) {
        // 1. 상품 찾기
        Product productPS = productRepository.findById(id).orElseThrow(() -> {
            throw new Exception400("id", "해당 상품을 찾을 수 없습니다.");
        });

        // 2. Update 더티체킹
        productPS.update(updateDTO.getName(), updateDTO.getPrice(), updateDTO.getQty());

        // em.flush(); // controller 레이어에서 작동 안함!! (거부)
        // productRepository.save(productPS); // em.merge() 가 실행됨

        // 3. 응답하기
        ResponseDto<?> responseDto = new ResponseDto<>().data(productPS);
        return ResponseEntity.ok(responseDto);
    } // 종료시에 트랜잭션 종료 - 변경감지 -> flush

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        // productRepository.deleteById(id); // 잠깐 delete하는 동안 DB에 락이 걸려서, 다른 쓰레드는 이 시간동안 insert, update, delete 못함

        Product productPS = productRepository.findById(id).orElseThrow(() -> {
            throw new Exception400("id", "해당 상품을 찾을 수 없습니다.");
        });
        productRepository.delete(productPS);
        ResponseDto<?> responseDto = new ResponseDto<>().data(productPS);
        return ResponseEntity.ok(responseDto);
    }
}
