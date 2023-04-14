package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 상품 등록, 상품 수정, 상품 삭제, 상품 목록 조회, 상품 상세 조회
 */
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductRepository productRepository;

    @PostMapping("/seller/products")
    public ResponseEntity<?> save(@RequestBody ProductRequest.saveDTO saveDTO) {
        if (saveDTO.getName().isBlank()) {
            throw new Exception400("name을 입력해주세요.");
        }
        if (saveDTO.getPrice() == null) {
            throw new Exception400("price를 입력해주세요.");
        }
        if (saveDTO.getQty() == null) {
            throw new Exception400("qty를 입력해주세요.");
        }

        Product productPS = productRepository.save(saveDTO.toEntity());

        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Transactional
    @PutMapping("/seller/products/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequest.updateDTO updateDTO) {
        if (updateDTO.getName().isBlank()) {
            throw new Exception400("name을 입력해주세요.");
        }
        if (updateDTO.getPrice() == null) {
            throw new Exception400("price를 입력해주세요.");
        }
        if (updateDTO.getQty() == null) {
            throw new Exception400("qty를 입력해주세요.");
        }

        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new Exception400("해당 상품을 찾을 수 없습니다.")
        );

        productPS.update(updateDTO.getName(), updateDTO.getPrice(), updateDTO.getQty());

        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new Exception400("해당 상품을 찾을 수 없습니다.")
        );

        productRepository.delete(productPS);

        ResponseDTO<?> responseDTO = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/products")
    public ResponseEntity<?> findAll() {
        List<Product> productListPS = productRepository.findAll();

        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productListPS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new Exception400("해당 상품을 찾을 수 없습니다.")
        );

        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
