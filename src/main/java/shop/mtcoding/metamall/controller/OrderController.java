package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.order.OrderRequest;
import shop.mtcoding.metamall.model.order.product.OrderProduct;
import shop.mtcoding.metamall.model.order.sheet.OrderSheet;
import shop.mtcoding.metamall.model.order.sheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 주문(고객), 주문 취소(고객), 주문 취소(판매자), 주문 목록 조회(고객), 주문 목록 조회(판매자)
 */
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderSheetRepository orderSheetRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HttpSession session;

    @Transactional
    @PostMapping("/user/orders")
    public ResponseEntity<?> save(@RequestBody OrderRequest.saveDTO saveDTO) {
        // 세션값으로 유저 찾기
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        User userPS = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new Exception400("해당 유저를 찾을 수 없습니다."));

        // 상품 찾기
        List<Product> productListPS = productRepository.findAllById(saveDTO.getIds());

        // 주문 상품 리스트 생성
        List<OrderProduct> orderProductListPS = saveDTO.toEntity(productListPS);

        // 주문서 생성
        Integer totalPrice = orderProductListPS.stream()
                .mapToInt((orderProduct) -> orderProduct.getOrderPrice()).sum();
        OrderSheet orderSheet = OrderSheet.builder()
                .user(userPS)
                .totalPrice(totalPrice)
                .build();
        OrderSheet orderSheetPS = orderSheetRepository.save(orderSheet);

        // 주문서에 상품 추가 및 재고 변경
        orderProductListPS.stream()
                .forEach((orderProductPS -> {
                    orderSheetPS.addOrderProduct(orderProductPS);
                    orderProductPS.getProduct().updateQty(orderProductPS.getCount());
                }));

        // 응답
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(orderSheetPS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
