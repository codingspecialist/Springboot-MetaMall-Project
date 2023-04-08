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
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final ProductRepository productRepository;
    private final HttpSession session;
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final UserRepository userRepository;

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

       if(role.equals(Role.USER.toString())){ // 고객일 경우 본인의 주문 목록 보기
           OrderSheet orderSheet = orderSheetRepository.findByUserId(userId).get();
           List<OrderProduct> orderProductList = orderSheet.getOrderProductList();
           ResponseDto<?> responseDto = new ResponseDto<>().data(orderProductList);
           return ResponseEntity.ok().body(responseDto);
       }else if(role.equals(Role.SELLER.toString())){ // 판매자일 경우 모든 주문 목록 보기
           List<OrderSheet> orderSheetList = orderSheetRepository.findAll();
           ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetList);
           return ResponseEntity.ok().body(responseDto);
       }else {
           throw new Exception400("고객만 주문 목록에 접근할 수 있습니다");
       }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancel(HttpServletRequest request){
        System.out.println("OrderController : cancel 호출됨 ");
        // 1. 사용자의 토큰 인증
        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        System.out.println( JwtProvider.HEADER + " - jwt upload : " + jwt);
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
        String role = decodedJWT.getClaim("role").asString();
        Long userId = decodedJWT.getClaim("id").asLong();

        if(role.equals(Role.USER.toString())) { // 고객일 경우 본인의 주문 목록 삭제하기 -> 빈 주문 목록으로 새로 생성해주기
            orderSheetRepository.deleteByUserId(userId);
            Optional<User> user = userRepository.findById(userId);
            OrderSheet orderSheet = OrderSheet.builder().user(user.get()).totalPrice(0).build();
            orderSheetRepository.save(orderSheet);
            ResponseDto<?> responseDto = new ResponseDto<>().data("주문이 정상적으로 취소되었습니다. ");
            return ResponseEntity.ok().body(responseDto);
        }else {
            throw new Exception400("잘못된 접근입니다. ");
        }
    }

    @DeleteMapping("/cancel/{userId}")
    public ResponseEntity<?> cancelSeller(@PathVariable Long userId, HttpServletRequest request){
        System.out.println("OrderController : cancelSeller 호출됨 ");
        // 1. 사용자의 토큰 인증
        String jwt = request.getHeader(JwtProvider.HEADER).replaceAll("Bearer ", "");
        System.out.println( JwtProvider.HEADER + " - jwt upload : " + jwt);
        DecodedJWT decodedJWT = JwtProvider.verify(jwt);

        // 2. 사용자의 권한 확인 SELLER(판매자), ADMIN(관리자)여야 등록 가능
        String role = decodedJWT.getClaim("role").asString();

        if(role.equals(Role.SELLER.toString())){ // 판매자일 경우 본인의 body에 입력한 고객의 주문 목록 삭제하기 -> 빈 주문 목록으로 새로 생성해주기
            // Long userId = Long.parseLong(id);
            orderSheetRepository.deleteByUserId(userId);
            User user = userRepository.findById(userId).orElseThrow(() -> {
                return new Exception400(userId + "번 고객을 찾을 수 없습니다. ");
            });
            OrderSheet orderSheet = OrderSheet.builder().user(user).totalPrice(0).build();
            orderSheetRepository.save(orderSheet);
            ResponseDto<?> responseDto = new ResponseDto<>().data(userId + "번 고객의 주문이 정상적으로 취소되었습니다. ");
            return ResponseEntity.ok().body(responseDto);
        }else {
            throw new Exception400("잘못된 접근입니다. ");
        }
    }
}
