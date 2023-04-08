package shop.mtcoding.metamall.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.order.OrderRequest;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final ProductRepository productRepository;
    private final HttpSession session;
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;

    @PostMapping("/order")
    @Transactional
    public ResponseEntity<?> order(@RequestBody OrderRequest.OrderDto orderDto, HttpServletRequest request) {

        System.out.println("OrderController : order 호출됨 ");
        String name = orderDto.getName();
        Integer count = orderDto.getCount();

        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        System.out.println( JwtProvider.HEADER + " - jwt : " + jwt);
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        // 사용자의 권한 확인 USER
        String role = decodedJWT.getClaim("role").asString();
        Long userId = decodedJWT.getClaim("id").asLong();
        System.out.println("Role : " + role);
        System.out.println("userId : " + userId);

        if(role.equals(Role.USER.toString())){ //USER 여야 주문가능
            Product productPS = productRepository.findByName(name).orElseThrow(() -> {
                return new Exception400("제품의 이름을 찾을 수 없습니다. ");
            });

            if(productPS.getQty() - count< 0){
                throw new Exception400("상품의 수량이 부족합니다. ");
            }
            productPS.setQty(productPS.getQty() - count); //수량 업데이트
            OrderProduct orderProduct = OrderProduct.builder().product(productPS).count(count).orderPrice(productPS.getPrice() * count).build();
            orderProductRepository.save(orderProduct);

            OrderSheet orderSheetPS = orderSheetRepository.findByUserId(userId).orElseThrow(() -> { // 주문서 찾기
                return new Exception400("잘못된 접근입니다.  ");
            });
            List<OrderProduct> orderProductList = orderSheetPS.getOrderProductList();
            orderProductList.add(orderProduct);
            orderSheetPS.setOrderProductList(orderProductList); //orderProduct 추가된 리스트로 업데이트


            Integer totalPrice = orderSheetPS.getTotalPrice();
            totalPrice = totalPrice + orderProduct.getOrderPrice();
            orderSheetPS.setTotalPrice(totalPrice);

            orderProduct.setOrderSheet(orderSheetPS.getId());

            System.out.println("권한 확인 완료");
            ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetPS);
            return ResponseEntity.ok().body(responseDto);
        }else {
            throw new Exception400("고객만 주문할 수 있습니다");
        }
    }

    @GetMapping("/findAllOrder")
    public ResponseEntity<?> findAllOrder(HttpServletRequest request) {
        System.out.println("OrderController : findAllOrder 호출됨 ");
        // 1. 사용자의 토큰 인증
       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
       System.out.println( JwtProvider.HEADER + " - jwt upload : " + jwt);
       DecodedJWT decodedJWT = JwtProvider.verify(jwt);

       // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
       String role = decodedJWT.getClaim("role").asString();
        Long userId = decodedJWT.getClaim("id").asLong();

       if(role.equals(Role.USER.toString())){
           OrderSheet orderSheet = orderSheetRepository.findByUserId(userId).get();
           List<OrderProduct> orderProductList = orderSheet.getOrderProductList();
           ResponseDto<?> responseDto = new ResponseDto<>().data(orderProductList);
           return ResponseEntity.ok().body(responseDto);
       }else if(role.equals(Role.SELLER.toString())){
           List<OrderSheet> orderSheetList = orderSheetRepository.findAll();
           ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetList);
           return ResponseEntity.ok().body(responseDto);
       }else {
           throw new Exception400("고객만 주문 목록에 접근할 수 있습니다");
       }
    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> upload(@RequestBody Product uploadProduct, HttpServletRequest request){
//        System.out.println("ProductController : upload 호출됨 ");
//       // 1. 사용자의 토큰 인증
//       String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
//       System.out.println( JwtProvider.HEADER + " - jwt upload : " + jwt);
//       DecodedJWT decodedJWT = JwtProvider.verify(jwt);
//
//       // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
//       String role = decodedJWT.getClaim("role").asString();
//        System.out.println("Role : " + role);
//       if(role.equals(Role.SELLER.toString())){
//           productRepository.save(uploadProduct);
//           System.out.println("권한 확인 완료");
//           ResponseDto<?> responseDto = new ResponseDto<>().data(uploadProduct);
//           return ResponseEntity.ok().body(responseDto);
//       }else{
//           throw new Exception400("판매자만 등록할 수 있습니다");
//       }
//    }

//    @Transactional
//    @PutMapping("/update/{bookname}")
//    public ResponseEntity<?> update(@PathVariable String bookname, @RequestBody Product updateProduct, HttpServletRequest request){
//        System.out.println("ProductController : update 호출됨 ");
//        // 1. 사용자의 토큰 인증
//        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
//        System.out.println( JwtProvider.HEADER + " - jwt update : " + jwt);
//        DecodedJWT decodedJWT = JwtProvider.verify(jwt);
//
//        // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
//        String role = decodedJWT.getClaim("role").asString();
//        System.out.println("Role : " + role);
//
//        if(role.equals(Role.SELLER.toString())){
//            System.out.println("권한 확인 완료");
//
//            Product productPS = productRepository.findByName(bookname).orElseThrow(() -> {
//                return new Exception400("제품의 이름을 찾을 수 없습니다. ");
//            }); //영속화 하기
//
//            productPS.setPrice(updateProduct.getPrice());
//            productPS.setQty(updateProduct.getQty());
//
//            ResponseDto<?> responseDto = new ResponseDto<>().data(updateProduct);
//            return ResponseEntity.ok().body(responseDto);
//
//        }else{
//            throw new Exception400("판매자만 등록할 수 있습니다");
//        }
//    }
//
//    @Transactional
//    @DeleteMapping("/delete/{name}")
//    public ResponseEntity<?> delete(@PathVariable String name, HttpServletRequest request){
//        System.out.println("ProductController : delete 호출됨 ");
//        // 1. 사용자의 토큰 인증
//        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
//        System.out.println( JwtProvider.HEADER + " - jwt delete : " + jwt);
//        DecodedJWT decodedJWT = JwtProvider.verify(jwt);
//
//        // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
//        String role = decodedJWT.getClaim("role").asString();
//        System.out.println("Role : " + role);
//
//        if(role.equals(Role.SELLER.toString())){
//            System.out.println("권한 확인 완료");
//
//            Product productPS = productRepository.findByName(name).orElseThrow(() -> {
//                return new Exception400("제품의 이름을 찾을 수 없습니다. ");
//            }); //제품이 있는지 확인
//
//            productRepository.deleteById(productPS.getId());
//
//            ResponseDto<?> responseDto = new ResponseDto<>().data("Delete Success!");
//            return ResponseEntity.ok().body(responseDto);
//
//        }else{
//            throw new Exception400("판매자만 삭제할 수 있습니다");
//        }
//    }
}
