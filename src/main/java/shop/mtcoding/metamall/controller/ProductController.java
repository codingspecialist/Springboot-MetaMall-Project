package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.config.auth.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductReqDto;
import shop.mtcoding.metamall.dto.product.ProductReqDto.ProductUpdateReqDto;
import shop.mtcoding.metamall.dto.product.ProductRespDto.ProductListRespDto;
import shop.mtcoding.metamall.dto.product.ProductRespDto.ProductListRespDto.ProductDto;
import shop.mtcoding.metamall.dto.product.ProductRespDto.ProductRegisterRespDto;
import shop.mtcoding.metamall.service.ProductService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping("/admin/product/register")
    public ResponseEntity<?> registerProduct(@RequestBody @Valid ProductReqDto.ProductRegisterReqDto productRegisterReqDto, BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser){
        ProductRegisterRespDto productRegisterRespDto = productService.상품등록(productRegisterReqDto);
        //누가 상품등록했는지 추후 추가하거나 로그 남기는 것 필요
        return new ResponseEntity<>(new ResponseDto<>(1,"상품 등록 완료", productRegisterRespDto), CREATED);
    }

    @GetMapping("/s/product")
    public ResponseEntity<?> listProduct(){
        ProductListRespDto productListRespDto=productService.상품목록();
        return new ResponseEntity<>(new ResponseDto<>(1,"상품목록 보기 완료", productListRespDto), CREATED);
    }
    @GetMapping("/a/product/{id}")
    public ResponseEntity<?> getProduct(Long id){
        ProductDto productdto = productService.상품상세(id);
        return new ResponseEntity<>(new ResponseDto<>(1,"상품상세 보기 완료", productdto), CREATED);
    }
    @PutMapping("/admin/product")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateReqDto productUpdateReqDto){
        ProductDto productDto = productService.상품수정(productUpdateReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"상품수정 완료", productDto), CREATED);
    }

    @DeleteMapping("/admin/product")
    public ResponseEntity<?> removeProduct(Long id){
        return new ResponseEntity<>(new ResponseDto<>(1,"상품삭제 완료", null), CREATED);
    }
}
