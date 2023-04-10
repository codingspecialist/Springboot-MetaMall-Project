package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception403;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.orderproduct.OrderProductRequest;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.Role;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final HttpSession session;

    @Transactional
    @PostMapping("/order/{id}") // 고객 권한
    public ResponseEntity<?> order(@PathVariable Integer id,
                                   @RequestBody OrderProductRequest.OrderDto orderDto) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser.getRole().equals(Role.CUSTOMER.getRole())) {
            Optional<User> userPS = userRepository.findById(loginUser.getId().longValue());
            Optional<Product> productPS = productRepository.findById(id.longValue());
            if (userPS.isPresent() && productPS.isPresent()) {
                User user = userPS.get();
                Product product = productPS.get();

                if (product.getQty() - orderDto.getCount() < 0) { // 재고 확인
                    throw new Exception400("주문하려는 상품의 수량이 재고보다 많습니다.");
                }
                product.updateQty(product.getQty() - orderDto.getCount()); // 재고 갱신
                orderDto.setProduct(product);
                OrderProduct orderProduct = orderProductRepository.save(orderDto.toEntity()); // 세팅 후 product 저장

                Optional<OrderSheet> orderSheetPS = orderSheetRepository.findByUserId(loginUser.getId().longValue());
                OrderSheet order;
                if (orderSheetPS.isPresent()) { // 기존에 해당 주문자의 주문이 있었을 경우
                    order = orderSheetPS.get();
                    order.updateList(orderProduct); // 주문서 리스트에 orderproduct 추가
                    order.updateTotalPrice(orderProduct.getOrderPrice()); // 주문서 totalPrice 갱신
                } else { // 기존에 해당 주문자의 주문이 존재하지 않았을 경우
                    order = orderSheetRepository.save(OrderSheet.builder()
                            .user(user)
                            .totalPrice(orderDto.getOrderPrice())
                            .build());
                }

                ResponseDto<?> responseDto = new ResponseDto<>().data(order);
                return ResponseEntity.ok().body(responseDto);
            } else {
                throw new Exception404("존재하지 않는 사용자 혹은 상품입니다.");
            }
        } else {
            throw new Exception403("권한이 없는 사용자입니다.");
        }
    }

    @GetMapping("/{id}/orders") // 권한에 따라 수행하는 작업이 달라짐
    public ResponseEntity<?> findOrders(@PathVariable Integer id) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        // 고객 권한, 본인의 주문목록만 접근 가능
        if (loginUser.getRole().equals(Role.CUSTOMER.getRole()) && loginUser.getId() == id) {
            Optional<OrderSheet> orderSheetListPS = orderSheetRepository.findByUserId(id.longValue());
            if (orderSheetListPS.isPresent()) {
                OrderSheet order = orderSheetListPS.get();

                ResponseDto<?> responseDto = new ResponseDto<>().data(order);
                return ResponseEntity.ok().body(responseDto);
            } else {
                throw new Exception404("존재하지 않는 사용자입니다.");
            }
            // 판매자 권한, 모든 주문목록에 접근 가능
        } else if (loginUser.getRole().equals(Role.SELLER.getRole())) {
            List<OrderSheet> orderSheetListPS = orderSheetRepository.findAll();

            ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetListPS);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception403("권한이 없는 사용자입니다.");
        }
    }

    @DeleteMapping("/{userId}/order/{orderId}") // 권한에 따라 수행하는 작업이 달라짐
    public ResponseEntity<?> withdraw(@PathVariable Integer userId,
                                      @PathVariable Integer orderId) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        // 고객 권한, 본인의 주문만 취소 가능
        if (loginUser.getRole().equals(Role.CUSTOMER.getRole()) && loginUser.getId() == userId) {
            Optional<OrderSheet> orderPS = orderSheetRepository.findByUserIdAndOrderId(userId.longValue(), orderId.longValue());
            if (orderPS.isPresent()) {
                OrderSheet order = orderPS.get();

                orderSheetRepository.deleteById(orderId.longValue());


            }
            // 고객 권한, 모든 주문 취소 가능
        } else if (loginUser.getRole().equals(Role.SELLER.getRole())) {

        } else {
            throw new Exception403("권한이 없는 사용자입니다.");
        }
    }
}
