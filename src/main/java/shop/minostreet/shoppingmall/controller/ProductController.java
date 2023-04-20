package shop.minostreet.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.dto.product.ProductReqDto;
import shop.minostreet.shoppingmall.dto.product.ProductReqDto.ProductUpdateReqDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductListRespDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductRegisterRespDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductUpdateRespDto;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;
import shop.minostreet.shoppingmall.repository.ProductRepository;
import shop.minostreet.shoppingmall.repository.UserRepository;
import shop.minostreet.shoppingmall.service.ProductService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @PostMapping("/seller/product/register")
    public ResponseEntity<?> registerProduct(@RequestBody @Valid ProductReqDto.ProductRegisterReqDto productRegisterReqDto, Errors errors, @AuthenticationPrincipal LoginUser loginUser){
        // 1. 판매자 찾기
        User sellerPS = userRepository.findById(loginUser.getUser().getId())
                .orElseThrow(
                        () -> new MyApiException("판매자를 찾을 수 없습니다")
                );
        ProductRegisterRespDto productRegisterRespDto = productService.상품등록(productRegisterReqDto, sellerPS);
        //checkPoint: 누가 상품등록했는지 추후 추가하거나 로그 남기는 것 필요
        return new ResponseEntity<>(new ResponseDto<>(1,"상품 등록 완료", productRegisterRespDto), OK);
    }

    @GetMapping("/user/product")
    public ResponseEntity<?> listProduct(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        ProductListRespDto productListRespDto=productService.상품목록(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1,"상품목록 보기 완료", productListRespDto), OK);
    }
    @GetMapping("/user/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        ProductDto productdto = productService.상품상세(id);
        return new ResponseEntity<>(new ResponseDto<>(1,"상품상세 보기 완료", productdto), OK);
    }

    @Transactional
    @PutMapping("/seller/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateReqDto productUpdateReqDto, Errors errors){
        Product productPS = productRepository.findById(id).orElseThrow(()-> new MyApiException("해당 상품을 찾을 수 없습니다"));

        productPS.update(productUpdateReqDto);
        ProductUpdateRespDto productUpdateRespDto=new ProductUpdateRespDto(productPS);

        return new ResponseEntity<>(new ResponseDto<>(1,"상품수정 완료", productUpdateRespDto), OK);
    }

    @DeleteMapping("/seller/product/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable Long id){
        //(1) 전달 받은 값을 확인하는 절차
        Product productPS = productRepository.findById(id).orElseThrow(() -> new MyApiException("해당 상품을 찾을 수 없습니다"));
        productService.상품삭제(productPS);
        return new ResponseEntity<>(new ResponseDto<>(1,"상품삭제 완료", null), OK);
    }
}
