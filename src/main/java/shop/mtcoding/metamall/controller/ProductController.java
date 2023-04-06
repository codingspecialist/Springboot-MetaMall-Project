package shop.mtcoding.metamall.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final HttpSession session;

    @GetMapping("/find")
    public ResponseEntity<?> find(@RequestBody ProductRequest.ProductDto product, HttpServletRequest request) {
        System.out.println("ProductController : find 호출됨 ");
        //인증만 필요
        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        System.out.println( JwtProvider.HEADER + " - jwt : " + jwt);
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        Optional<Product> productOP = productRepository.findByName(product.getName());
        if (productOP.isPresent()) {
            // 1. 물건 정보 꺼내기
            Product findProduct = productOP.get();

            // 2. 응답 DTO 생성
            ResponseDto<?> responseDto = new ResponseDto<>().data(findProduct);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception400("잘못된 요청입니다");
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        System.out.println("ProductController : findAll 호출됨 ");
        //인증만 필요
       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
       System.out.println( JwtProvider.HEADER + " - jwt : " + jwt);
       DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        List<Product> productOP = productRepository.findAll();
        ResponseDto<?> responseDto = new ResponseDto<>().data(productOP);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody Product uploadProduct, HttpServletRequest request){
        System.out.println("ProductController : upload 호출됨 ");
       // 1. 사용자의 토큰 인증
       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
       System.out.println( JwtProvider.HEADER + " - jwt upload : " + jwt);
       DecodedJWT decodedJWT = JwtProvider.verify(jwt);

       // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
       String role = decodedJWT.getClaim("role").asString();
        System.out.println("Role : " + role);
       if(role.equals(Role.SELLER.toString())){
           productRepository.save(uploadProduct);
           System.out.println("권한 확인 완료");
           ResponseDto<?> responseDto = new ResponseDto<>().data(uploadProduct);
           return ResponseEntity.ok().body(responseDto);
       }else{
           throw new Exception400("판매자만 등록할 수 있습니다");
       }
    }
}
