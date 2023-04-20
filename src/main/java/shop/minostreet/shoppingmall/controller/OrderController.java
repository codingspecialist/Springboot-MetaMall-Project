package shop.minostreet.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.minostreet.shoppingmall.config.auth.LoginUser;
import shop.minostreet.shoppingmall.domain.OrderProduct;
import shop.minostreet.shoppingmall.domain.OrderSheet;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.dto.ResponseDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderReqDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.OrderListBySellerRespDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.OrderListRespDto;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.SaveRespDTO;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;
import shop.minostreet.shoppingmall.handler.exception.MyForbiddenException;
import shop.minostreet.shoppingmall.repository.OrderProductRepository;
import shop.minostreet.shoppingmall.repository.OrderSheetRepository;
import shop.minostreet.shoppingmall.repository.ProductRepository;
import shop.minostreet.shoppingmall.repository.UserRepository;
import shop.minostreet.shoppingmall.service.OrderService;

import javax.validation.Valid;
import java.util.List;


/**
     * 주문하기(고객), 주문목록보기(고객), 주문목록보기(판매자), 주문취소하기(고객), 주문취소하기(판매자)
     */
    @RequiredArgsConstructor
    @RestController
    public class OrderController {
        private final OrderProductRepository orderProductRepository;
        private final OrderSheetRepository orderSheetRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;
        private final OrderService orderService;

        @Transactional
        @PostMapping("/orders")
        public ResponseEntity<?> save(@RequestBody @Valid OrderReqDto.SaveReqDTO saveReqDTO, Errors errors, @AuthenticationPrincipal LoginUser loginUser) {
            //checkpoint : 해당 상품번호가 없어도 성공하는 에러 체크

            // 1. 세션값으로 유저 찾기
            User userPS = userRepository.findById(loginUser.getUser().getId()).orElseThrow(() -> new MyApiException("해당 유저를 찾을 수 없습니다"));

            // 2. 상품 찾기
            if(saveReqDTO.getIds().size()==0) throw new MyApiException("주문할 수량이 0개일 수 없습니다.");

            //3. 서비스 호출
            SaveRespDTO saveRespDTO = orderService.주문등록(saveReqDTO, userPS);

            // 4. 응답하기
            return new ResponseEntity<>(new ResponseDto<>(1, "주문등록 완료", saveRespDTO), HttpStatus.OK);
        }

        // 유저 주문서 조회
        @GetMapping("/orders")
        public ResponseEntity<?> findByUserId(@AuthenticationPrincipal LoginUser loginUser) {
            OrderListRespDto orderListRespDto = orderService.주문목록조회(loginUser);
            return new ResponseEntity<>(new ResponseDto<>(1, "유저 주문목록조회 완료", orderListRespDto), HttpStatus.OK);
        }

        // 그림 설명 필요!!
        // 배달의 민족은 하나의 판매자에게서만 주문을 할 수 있다. (다른 판매자의 상품이 담기면, 하나만 담을 수 있게 로직이 변한다)
        // 쇼핑몰은 여러 판매자에게서 주문을 할 수 있다.

        // 판매자 주문서 조회
        @GetMapping("/seller/orders")
        public ResponseEntity<?> findBySellerId() {
            //여기서는 판매자 한명이므로 유저 정보 전달 안함 -> 권한 체크만 하면 됨.
            OrderListBySellerRespDto orderListBySellerRespDto = orderService.판매자주문목록조회();

            return new ResponseEntity<>(new ResponseDto<>(1, "판매자 주문목록조회 완료", orderListBySellerRespDto), HttpStatus.OK);
        }

        // 유저 주문 취소
        @DeleteMapping("/orders/{id}")
        public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
            // 1. 주문서 찾기
            OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(() -> new MyApiException("해당 주문을 찾을 수 없습니다"));

            // 2. 해당 주문서의 주인 여부 확인
            if (!orderSheetPS.getUser().getId().equals(loginUser.getUser().getId())) {
                throw new MyForbiddenException("권한이 없습니다");
            }

            //3. 응답하기
//            ResponseDto<?> ResponseDto = new ResponseDto<>();
//            return ResponseEntity.ok().body(ResponseDto);
            return new ResponseEntity<>(new ResponseDto<>(1, "유저 주문취소 완료", null), HttpStatus.OK);
        }

        // 판매자 주문 취소
        @DeleteMapping("/seller/orders/{id}")
        public ResponseEntity<?> deleteSeller(@PathVariable Long id) {
            // 1. 주문서 찾기
            OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(() -> new MyApiException("해당 주문을 찾을 수 없습니다"));

            orderService.판매자주문취소(orderSheetPS);
            return new ResponseEntity<>(new ResponseDto<>(1, "판매자 주문취소 완료", null), HttpStatus.OK);
    }
}
