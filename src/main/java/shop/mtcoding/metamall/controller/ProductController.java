package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 상품등록, 상품목록보기, 상품상세보기, 상품수정하기, 상품삭제하기
 */
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final HttpSession session;

    @PostMapping("/seller/products")
    public ResponseEntity<?> save(@RequestBody @Valid ProductRequest.SaveDTO saveDTO, Errors errors){
        Product productPS = productRepository.save(saveDTO.toEntity());
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Product> productPagePS = productRepository.findAll(pageable);
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPagePS);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Product productPS = productRepository.findById(id).orElseThrow(()-> new Exception400("id", "해당 상품을 찾을 수 없습니다"));
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @Transactional
    @PutMapping("/seller/products/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProductRequest.UpdateDTO updateDTO, Errors errors){
        Product productPS = productRepository.findById(id).orElseThrow(()-> new Exception400("id", "해당 상품을 찾을 수 없습니다"));
        productPS.update(updateDTO.getName(), updateDTO.getPrice(), updateDTO.getQty());
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(productPS);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/seller/products/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Product productPS = productRepository.findById(id).orElseThrow(()-> new Exception400("id", "해당 상품을 찾을 수 없습니다"));
        productRepository.delete(productPS);
        ResponseDTO<?> responseDto = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDto);
    }
}
