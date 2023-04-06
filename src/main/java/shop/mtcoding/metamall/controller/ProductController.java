package shop.mtcoding.metamall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.board.ProductRequest;
import shop.mtcoding.metamall.dto.user.UserRequest;
import shop.mtcoding.metamall.model.log.login.LoginLog;
import shop.mtcoding.metamall.model.log.login.LoginLogRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final HttpSession session;

    @GetMapping("/find")
    public ResponseEntity<?> login(@RequestBody ProductRequest.ProductDto productDto, HttpServletRequest request) {

        //인증만 필요
            String jwt = request.getHeader(JwtProvider.HEADER).replace("Bearer ", "");
            System.out.println(jwt);
            DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        Optional<Product> productOP = productRepository.findByName(productDto.getName());
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

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody Product uploadProduct){
        try {
            // 1. 사용자의 토큰 인증
            String jwt = (String) session.getAttribute(JwtProvider.HEADER);
            DecodedJWT decode = JwtProvider.verify(jwt);

            // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
            String role = decode.getClaim("role").asString();
            if(role.equals(Role.SELLER)){
                productRepository.save(uploadProduct);
                System.out.println("ProductController : upload 호출됨 ");
                ResponseDto<?> responseDto = new ResponseDto<>().data(uploadProduct);
                return ResponseEntity.ok().body(responseDto);
            }else{
                throw new Exception400("판매자만 등록할 수 있습니다");
            }
        }catch (Exception e){
            throw new Exception400("잘못된 요청입니다");
        }
    }
}
