package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.annotation.Auth;
import shop.mtcoding.metamall.core.annotation.RoleCk;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.order.OrderRequest;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Transactional
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderSheetRepository orderSheetRepository;
    private final ProductRepository productRepository;

    /**
     * 주문(고객)
     */
    @RoleCk.User
    @PostMapping("/order")
    public ResponseEntity<?> order(@Auth User user, @RequestBody OrderRequest.OrderDto orderDto){
        List<OrderProduct> orders = orderDto.getProducts().stream()
                .map(dto -> {
                    Product product = productRepository.findById(dto.getProductId()).orElseThrow(
                            () -> new Exception404("상품을 찾을 수 없습니다"));
                    product.order(dto.getCount());
                    return OrderProduct.builder()
                            .product(product)
                            .count(dto.getCount())
                            .orderPrice(product.getPrice()*dto.getCount())
                            .build();
                }).collect(Collectors.toList());
        int totalPrice = orders.stream().mapToInt(OrderProduct::getOrderPrice).sum();
        OrderSheet orderSheet = OrderSheet.builder()
                .user(user)
                .totalPrice(totalPrice)
                .build();
        orders.forEach(order -> order.syncOrderSheet(orderSheet));
        orderSheetRepository.save(orderSheet);
        return ResponseEntity.ok().body(new ResponseDto<>().data(orderSheet));
    }

    /**
     * 주문목록
     */
    @Transactional(readOnly = true)
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@Auth User user){
        List<OrderSheet> orderSheets = new ArrayList<>();
        if(user.getRole() == User.Role.USER) orderSheets = orderSheetRepository.findByUser(user);
        else if(user.getRole() == User.Role.SELLER) orderSheets = orderSheetRepository.findBySeller(user);
        return ResponseEntity.ok().body(new ResponseDto<>().data(orderSheets));
    }

    /**
     * 모든 주문목록(관리자)
     */
    @RoleCk.Admin
    @Transactional(readOnly = true)
    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAllOrders(@Auth User user){
        List<OrderSheet> orderSheets = orderSheetRepository.findAll();
        return ResponseEntity.ok().body(new ResponseDto<>().data(orderSheets));
    }

    /**
     * 주문취소
     */
    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> cancelOrder(@Auth User user, @PathVariable Long id){
        OrderSheet orderSheet = null;
        if(user.getRole() == User.Role.USER) orderSheet = orderSheetRepository.findByIdAndUser(id,user).orElseThrow(
                () -> new Exception404("주문이 없습니다"));
        else if(user.getRole() == User.Role.SELLER) orderSheet = orderSheetRepository.findByIdAndSeller(id,user).orElseThrow(
                () -> new Exception404("주문이 없습니다"));
        orderSheet.getOrderProductList().forEach(op -> op.getProduct().cancelOrder(op.getCount()));
        orderSheetRepository.delete(orderSheet);
        return ResponseEntity.ok().body(new ResponseDto<>().data(orderSheet));
    }
}
