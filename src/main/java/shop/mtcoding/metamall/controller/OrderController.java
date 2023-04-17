package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.dto.order.OrderProductRequest;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpSession session;

    // 상품 주문하기
    @PostMapping("/save/{id}")
    public ResponseEntity<?> save(
            @PathVariable Long id,
            @RequestBody OrderProductRequest.OrderDto orderDto, HttpServletRequest request) {

        Optional<User> userOP = userRepository.findById(id);
        SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");

        return null;
    }

    // 주문 목록 보기 - 사용자
    @GetMapping("/list")
    public ResponseEntity<?> orderList() {
/*
        List<OrderProduct> orderList = orderProductRepository.findAll();

        if(orderList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("주문 물품이 존재하지 않습니다");

        ResponseDto<List<OrderProduct>> responseDto = new ResponseDto<>();
        responseDto.satData(orderList);

        return ResponseEntity.ok().body(responseDto);

 */
        return null;
    }
}
