package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.UserRepository;

/**
 * 상품 등록, 상품 수정, 상품 삭제, 상품 목록 조회, 상품 상세 조회
 */
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

}
