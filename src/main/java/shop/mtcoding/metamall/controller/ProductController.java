package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;

    // 상품 등록
    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestBody ProductRequest.ProductDto productDto,
            HttpServletRequest request) {

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .qty(productDto.getQuantity())
                .build();

        productRepository.save(product);
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(product);

        return ResponseEntity.ok().body(responseDto);
    }

    // 상품 전체 조회
    @GetMapping("/listAll")
    public ResponseEntity<?> productList() {

        List<Product> productList = productRepository.findAll();

        if(productList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("물품이 존재하지 않습니다");

        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productList);

        return ResponseEntity.ok().body(responseDto);
    }

    // 상품 개별 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        Optional<Product> productOP = productRepository.findById(id);

        if(productOP.isPresent()) {
            ResponseDTO<?> responseDto = new ResponseDTO<>().data(productOP);
            return ResponseEntity.ok().body(responseDto);
        }

        ResponseDTO<?> responseDto =
                new ResponseDTO<>().fail(
                        HttpStatus.BAD_REQUEST,
                        "Product Not Found",
                        "존재하지 않는 물품입니다");

        return ResponseEntity.ok().body(responseDto);
    }

    // 상품 개별 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> modify(
            @PathVariable Long id,
            @RequestBody ProductRequest.ProductDto productDto) {

        Optional<Product> productOP = productRepository.findById(id);

        if(productOP.isPresent()) {

            //roduct product = productOP.get();

            Product product = Product.builder()
                    .name(productDto.getName())
                    .price(productDto.getPrice())
                    .qty(productDto.getQuantity())
                    .build();

            Product savedProduct = productRepository.save(product);
            productRepository.flush();

            ResponseDTO<?> responseDto = new ResponseDTO<>().data(savedProduct);
            return ResponseEntity.ok().body(responseDto);
        }

        ResponseDTO<?> responseDto =
                new ResponseDTO<>().fail(
                        HttpStatus.BAD_REQUEST,
                        "Product Not Found",
                        "존재하지 않는 물품입니다");

        return ResponseEntity.ok().body(responseDto);
    }

    // 상품 개별 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestBody ProductRequest.ProductDto productDto) {

        Optional<Product> productOP = productRepository.findById(id);

        if (productOP.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().body("삭제 완료 되었습니다");
        }

        ResponseDTO<?> responseDto =
                new ResponseDTO<>().fail(
                        HttpStatus.BAD_REQUEST,
                        "Product Not Found",
                        "존재하지 않는 물품입니다");

        return ResponseEntity.ok().body(responseDto);
    }
}