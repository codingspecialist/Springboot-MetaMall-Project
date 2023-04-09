package shop.mtcoding.metamall.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception403;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final HttpSession session;

    @PostMapping("/find")
    public ResponseEntity<?> find(@Valid @RequestBody ProductRequest.ProductDto product, HttpServletRequest request) {
        //인증만 필요
        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);
        Long userId = decodedJWT.getClaim("id").asLong();

        Optional<Product> productOP = productRepository.findByName(product.getProductname());
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

       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
       DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        List<Product> productOP = productRepository.findAll();
        ResponseDto<?> responseDto = new ResponseDto<>().data(productOP);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@Valid @RequestBody Product uploadProduct, HttpServletRequest request){

       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
       DecodedJWT decodedJWT = JwtProvider.verify(jwt);

       Long userId = decodedJWT.getClaim("id").asLong();

        // 사용자의 권한 확인 SELLER(판매자) 등록 가능
       String role = decodedJWT.getClaim("role").asString();

       if(role.equals(Role.SELLER.toString())){
           productRepository.save(uploadProduct);
           ResponseDto<?> responseDto = new ResponseDto<>().data(uploadProduct);
           return ResponseEntity.ok().body(responseDto);
       }else{
           throw new Exception403("판매자만 물건을 등록할 수 있습니다");
       }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody Product updateProduct, HttpServletRequest request){

        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        // 사용자의 권한 확인 SELLER(판매자)
        String role = decodedJWT.getClaim("role").asString();
        Long userId = decodedJWT.getClaim("id").asLong();

        if(role.equals(Role.SELLER.toString())){

            Product productPS = productRepository.findByName(updateProduct.getProductname()).orElseThrow(() -> {
                return new Exception400("제품의 이름을 찾을 수 없습니다. ");
            }); //영속화 하기

            productPS.setPrice(updateProduct.getPrice());
            productPS.setQty(updateProduct.getQty());

            ResponseDto<?> responseDto = new ResponseDto<>().data(productPS);
            return ResponseEntity.ok().body(responseDto);

        }else{
            throw new Exception403("잘못된 접근입니다. ");
        }
    }


    @DeleteMapping("/delete/{productname}") // 값 안들어오면 404 반환
    public ResponseEntity<?> delete(@PathVariable(required = false) String productname, HttpServletRequest request){

        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);
        Long userId = decodedJWT.getClaim("id").asLong();

        // 사용자의 권한 확인 SELLER(판매자)
        String role = decodedJWT.getClaim("role").asString();

        if(role.equals(Role.SELLER.toString())){
            Product productPS = productRepository.findByName(productname).orElseThrow(() -> {
                return new Exception400("제품의 이름을 찾을 수 없습니다. ");
            }); //제품이 있는지 확인

            productRepository.deleteById(productPS.getId());

            ResponseDto<?> responseDto = new ResponseDto<>().data("Delete Success!");
            return ResponseEntity.ok().body(responseDto);

        }else{
            throw new Exception403("잘못된 접근입니다. ");
        }
    }
}
