package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final HttpSession session;

    @PostMapping("/product")
    public ResponseEntity<?> register(@RequestBody ProductRequest.RegisterDto registerDto) {
        productRepository.save(Product.builder()
                .name(registerDto.getName())
                .price(registerDto.getPrice())
                .qty(registerDto.getQty())
                .build());

        Optional<Product> productOP = productRepository.findByName(registerDto.getName());
        if (productOP.isPresent()) {
            Product registerProduct = productOP.get();

            ResponseDto<?> responseDto = new ResponseDto<>().data(registerProduct);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("상품 등록이 처리되지 않았습니다.");
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> findAll() {
        List<Product> productListPS = productRepository.findAll();

        ResponseDto<?> responseDto = new ResponseDto<>().data(productListPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Product> productPS = productRepository.findById(id.longValue());
        if (productPS.isPresent()) {
            Product findProduct = productPS.get();

            ResponseDto<?> responseDto = new ResponseDto<>().data(findProduct);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 상품입니다.");
        }
    }
}
