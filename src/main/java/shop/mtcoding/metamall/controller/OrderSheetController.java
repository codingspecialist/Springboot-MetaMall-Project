package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.orderproduct.OrderProductRequest;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class OrderSheetController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final HttpSession session;

    @PostMapping("/{userId}/order/{productId}")
    public ResponseEntity<?> order(@PathVariable Integer userId,
                                   @PathVariable Integer productId,
                                   @RequestBody OrderProductRequest.OrderDto orderDto) {
        Optional<User> userPS = userRepository.findById(userId.longValue());
        Optional<Product> productPS = productRepository.findById(productId.longValue());
        if (userPS.isPresent() && productPS.isPresent()) {
            User user = userPS.get();
            Product product = productPS.get();

            orderDto.setProduct(product);
            orderProductRepository.save(orderDto.toEntity());

            Optional<List<OrderSheet>> orderSheetListPS = orderSheetRepository.findByUserId(userId.longValue());
            OrderSheet newOrderSheet;
            if (orderSheetListPS.isPresent()) {
                List<OrderSheet> sheets = orderSheetListPS.get();
                for (int i = 0; i < sheets.size(); i++) {
                    OrderSheet sheet = sheets.get(i);
                    // dirty checking
                    sheet.update(orderDto.getOrderPrice());
                }
                newOrderSheet = orderSheetRepository.save(OrderSheet.builder()
                        .user(user)
                        .totalPrice(sheets.get(0).getTotalPrice())
                        .build());
            } else { // 기존에 해당 주문자의 주문 상품이 존재하지 않았을 경우
                newOrderSheet = orderSheetRepository.save(OrderSheet.builder()
                        .user(user)
                        .totalPrice(orderDto.getOrderPrice())
                        .build());
            }

            ResponseDto<?> responseDto = new ResponseDto<>().data(newOrderSheet);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 사용자 혹은 상품입니다.");
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> findBuyerOrders(@PathVariable Integer id) {
        Optional<List<OrderSheet>> orderSheetListPS = orderSheetRepository.findByUserId(id.longValue());
        if (orderSheetListPS.isPresent()) {
            List<OrderSheet> orderSheets = orderSheetListPS.get();

            ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheets);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 사용자입니다.");
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> findSellerOrders(@PathVariable Integer id) {
        Optional<List<OrderSheet>> orderSheetListPS = orderSheetRepository.findByUserId(id.longValue());
        if (orderSheetListPS.isPresent()) {
            List<OrderSheet> orderSheets = orderSheetListPS.get();

            ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheets);
            return ResponseEntity.ok().body(responseDto);
        } else {
            throw new Exception404("존재하지 않는 사용자입니다.");
        }
    }
}
