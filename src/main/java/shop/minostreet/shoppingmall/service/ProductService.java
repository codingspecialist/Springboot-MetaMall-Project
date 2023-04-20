package shop.minostreet.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.dto.product.ProductReqDto;

import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductListRespDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductRegisterRespDto;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;
import shop.minostreet.shoppingmall.repository.ProductRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //서비스는 DTO를 요청받고 DTO로 응답한다.
    @Transactional
    //메서드 시작할 때 트랜잭션 시작
    //메서드 종료시 트랜잭션 함께 종료
    /**
     * 상품등록 로직 -상품 이름, 패스워드, 이메일, 이름 필요
     * 1. 상품 이름 중복 체크
     * 2. 패스워드 인코딩
     * 3. dto 응답
     */
    public ProductRegisterRespDto 상품등록(@Valid ProductReqDto.ProductRegisterReqDto productRegisterReqDto, @AuthenticationPrincipal LoginUser loginUser){
//     1. 상품 이름 중복 체크
        Optional<Product> productOP = productRepository.findByName(productRegisterReqDto.getName());
        if(productOP.isPresent()){
            //중복된 상품이 존재하는 경우 예외발생
            throw new MyApiException("동일한 이름의 상품이 존재합니다.");
        }
        //2. 상품 등록
        Product productPS = productRepository.save(productRegisterReqDto.toEntity(loginUser.getUser()));
//     3. dto 응답
        return new ProductRegisterRespDto(productPS);

    }

    public ProductListRespDto 상품목록(Pageable pageable) {
        Page<Product> productListPS=productRepository.findAll(pageable);
        ///checkpoint: 나중에 페이징 처리 필요
        return new ProductListRespDto(productListPS);
    }

    public ProductDto 상품상세(Long id) {
        //해당 아이디의 상품 존재 확인
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new MyApiException("해당 상품이 존재하지 않습니다.")
        );
        return new ProductDto(productPS);
    }
    @Transactional
    public ProductDto 상품수정(@Valid ProductReqDto.ProductUpdateReqDto productUpdateReqDto){

        Product productPS=productRepository.save(productUpdateReqDto.toEntity());
        productPS.update(productUpdateReqDto);

        return new ProductDto(productPS);
    }
    @Transactional
    public void 상품삭제(Long id){
        //해당 아이디의 상품 존재 확인
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new MyApiException("해당 상품이 존재하지 않습니다.")
        );
        productRepository.deleteById(id);
    }
}
