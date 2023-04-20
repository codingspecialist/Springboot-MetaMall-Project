package shop.minostreet.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.domain.OrderProduct;
import shop.minostreet.shoppingmall.domain.OrderSheet;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderReqDto.SaveReqDTO;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.OrderListBySellerRespDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.OrderListRespDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.SaveRespDTO;
import shop.minostreet.shoppingmall.repository.OrderProductRepository;
import shop.minostreet.shoppingmall.repository.OrderSheetRepository;
import shop.minostreet.shoppingmall.repository.ProductRepository;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderSheetRepository orderSheetRepository;

    @Transactional
    public SaveRespDTO 주문등록(@Valid SaveReqDTO saveReqDTO, User userPS) {
        // 1. 주문 상품
        List<Product> productListPS = productRepository.findAllById(saveReqDTO.getIds());

        List<OrderProduct> orderProductListPS = saveReqDTO.toEntity(productListPS);

        // 2. 주문서 만들기
        Integer totalPrice = orderProductListPS.stream().mapToInt((orderProduct) -> orderProduct.getOrderPrice()).sum();
        OrderSheet orderSheet = OrderSheet.builder().user(userPS).totalPrice(totalPrice).build();
        OrderSheet orderSheetPS = orderSheetRepository.save(orderSheet);    //영속화된 상태의 주문서

        // 3. 주문서에 상품추가하고 재고감소하기
        orderProductListPS.stream().forEach((orderProductPS -> {
            orderProductPS.getProduct().updateQty(orderProductPS.getCount());
        }));
        return new SaveRespDTO(orderProductListPS, orderSheetPS);
    }

    public OrderListRespDto 주문목록조회(@AuthenticationPrincipal LoginUser loginUser) {

        List<OrderSheet> orderSheetListPS = orderSheetRepository.findByUserId(loginUser.getUser().getId());
        return new OrderListRespDto(orderSheetListPS);
    }

    public OrderListBySellerRespDto 판매자주문목록조회(){
        // 판매자는 한명이기 때문에 orderProductRepository.findAll() 해도 된다.
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findAll();
        return new OrderListBySellerRespDto(orderSheetListPS);
    }

    public void 판매자주문취소(OrderSheet orderSheetPS){
        // 1. 재고 변경하기
        List<OrderProduct> orderProductList= orderProductRepository.findByOrderSheetId(orderSheetPS.getId());
        orderProductList.stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
//            orderProductRepository.save(orderProduct); //더티체킹하겠지?
        });

        // 2. 주문서 삭제하기
        orderSheetRepository.delete(orderSheetPS);
    }

    public void 유저주문취소(OrderSheet orderSheetPS) {
        //1. 주문 상품목록 조회
        List<OrderProduct> orderProductList= orderProductRepository.findByOrderSheetId(orderSheetPS.getId());
        //2. 재고 변경하기
        orderProductList.stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
            //더티체킹하겠지??
        });

        //3. 주문서 삭제하기
        orderSheetRepository.delete(orderSheetPS);
    }
}
