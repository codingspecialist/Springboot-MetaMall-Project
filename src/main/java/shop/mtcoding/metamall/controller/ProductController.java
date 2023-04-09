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
        productRepository.save(registerDto.toEntity());

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

    @PutMapping("/product/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProductRequest.UpdateDto updateDto) {
        Optional<Product> productPS = productRepository.findById(id.longValue());
        if (productPS.isPresent()) {
            Product product = productPS.get();
            // 입력하지 않은 부분은 기존 값 유지
            if (updateDto.getName() == null || updateDto.getName().isBlank()) {
                updateDto.setName(product.getName());
            }
            if (updateDto.getPrice() == null) {
                updateDto.setPrice(product.getPrice());
            }
            if (updateDto.getQty() == null) {
                updateDto.setQty(product.getQty());
            }

            // dirty checking
            product.update(updateDto.toEntity());

            ResponseDto<?> responseDto = new ResponseDto<>().data(product);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 상품입니다.");
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Product> productPS = productRepository.findById(id.longValue());
        if (productPS.isPresent()) {
            Product product = productPS.get();

            productRepository.delete(product);

            ResponseDto<?> responseDto = new ResponseDto<>().data(product.getName() + " 상품 삭제가 완료되었습니다.");
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 상품입니다.");
        }
    }
}
