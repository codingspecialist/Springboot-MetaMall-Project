package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final HttpSession session;

    @PostMapping("/save") //판매자 권한
    public ResponseEntity<?> productSave(@RequestBody ProductRequest.SaveDto saveDto){

        //상품재고에 대한 if 써야할지 고민...
        Product productPS = productRepository.save(saveDto.toEntity());
        return new ResponseEntity<>(productPS, HttpStatus.CREATED);
    }

    @PostMapping("/update/name")
    public ResponseEntity<?> productUpdateName(@RequestBody ProductRequest.UpdateDto updateDto){
        Product productPS = productRepository.updateName(updateDto);

        return new ResponseEntity<>(productPS, HttpStatus.OK);
    }
    @PostMapping("/update/qty")
    public ResponseEntity<?> productUpdateQty(@RequestBody ProductRequest.UpdateDto updateDto){
        Product productPS = productRepository.updateQty(updateDto);

        return new ResponseEntity<>(productPS, HttpStatus.OK);
    }
    @PostMapping("/update/price")
    public ResponseEntity<?> productUpdatePrice(@RequestBody ProductRequest.UpdateDto updateDto){
        Product productPS = productRepository.updatePrice(updateDto);

        return new ResponseEntity<>(productPS, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> productList(){
        List<Product> productList = productRepository.findAll();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> productDetail(@PathVariable("id") Long id){
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new Exception400("id 확인해주세요") // 일단 RuntimeException 터트려놨는데 나중에 MyException으로 수정
        );
        return new ResponseEntity<>(productPS, HttpStatus.OK);
    }

    @DeleteMapping("/deleted/{id}") // 리턴갑 생각해보기
    public void productDeleted(@PathVariable("id") Long id){
        productRepository.deleteById(id);
    }
}
