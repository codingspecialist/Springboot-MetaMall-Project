package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.annotation.Auth;
import shop.mtcoding.metamall.core.annotation.RoleCk;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;

import java.util.List;

@Validated
@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * 상품등록(판매자)
     */
    @RoleCk.Seller
    @PostMapping("/product")
    public ResponseEntity<?> registerProduct(@Auth User seller, @RequestBody ProductRequest.ProductDto productDto){
        Product product = productRepository.save(Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .qty(productDto.getQty())
                .user(seller)
                .build());
        return ResponseEntity.ok().body(new ResponseDto<>().data(product));
    }

    /**
     * 상품목록
     */
    @Transactional(readOnly = true)
    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@Auth User user){
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok().body(new ResponseDto<>().data(products));
    }

    /**
     * 상품상세
     */
    @Transactional(readOnly = true)
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductDetails(@Auth User user, @PathVariable Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("상품을 찾을 수 없습니다"));
        return ResponseEntity.ok().body(new ResponseDto<>().data(product));
    }

    /**
     * 상품수정(판매자)
     */
    @RoleCk.Seller
    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@Auth User user, @PathVariable Long id, @RequestBody ProductRequest.ProductDto dto){
        Product product = productRepository.findByIdAndUser(id,user).orElseThrow(
                () -> new Exception404("상품을 찾을 수 없습니다"));
        product.update(dto);
        return ResponseEntity.ok().body(new ResponseDto<>().data(product));
    }
}
