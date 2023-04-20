package shop.minostreet.shoppingmall.dto.orderproduct;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import shop.minostreet.shoppingmall.domain.OrderProduct;
import shop.minostreet.shoppingmall.domain.OrderSheet;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderReqDto.SaveReqDTO;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderReqDto.SaveReqDTO.OrderProductDTO;
import shop.minostreet.shoppingmall.dto.orderproduct.OrderRespDto.OrderListRespDto.OrderSheetDto;
import shop.minostreet.shoppingmall.dto.product.ProductRespDto.ProductDto;
import shop.minostreet.shoppingmall.util.MyDateUtil;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRespDto {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Getter
    @Setter
    public static class SaveRespDTO {
        private Long id;
        private String username; // 주문자
        private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
        private String createdAt;
        private List<OrderDto> orderProductListPS;


        public SaveRespDTO(List<OrderProduct> orderProductListPS, OrderSheet orderSheetPS) {
            this.orderProductListPS=orderProductListPS.stream().map(
                    OrderDto::new
            ).collect(Collectors.toList());
            this.totalPrice = orderSheetPS.getTotalPrice();
            this.id=orderSheetPS.getId();
            this.createdAt= MyDateUtil.toStringFormat(orderSheetPS.getCreatedAt());
            this.username=orderSheetPS.getUser().getUsername();
        }
        @Getter
        @Setter
        public static class OrderDto{
            private Long id;
//            private Product product;
            private  String productName;

            private Integer count; // 상품 주문 개수
            private Integer orderPrice; // 상품 주문 금액

            public OrderDto(OrderProduct orderProduct) {
                this.id = orderProduct.getProduct().getId();
                this.productName = orderProduct.getProduct().getName();
                this.count = orderProduct.getCount();
                this.orderPrice = orderProduct.getOrderPrice();
            }
        }
    }

    @Getter
    @Setter
    public static class OrderListRespDto{
        private List<OrderSheetDto> orderList;
        public OrderListRespDto(List<OrderSheet> orders) {
            this.orderList = orders.stream().map(
                    OrderSheetDto::new
            ).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class OrderSheetDto{
            private Long id;
            private String username; // 주문자
            private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
            private LocalDateTime createdAt;

            public OrderSheetDto(OrderSheet orderSheet) {
                this.id = orderSheet.getId();
                this.username = orderSheet.getUser().getUsername();
                this.totalPrice = orderSheet.getTotalPrice();
                this.createdAt = orderSheet.getCreatedAt();
            }
        }
    }
    @Getter
    @Setter
    public static class OrderListBySellerRespDto{
        private List<OrderListRespDto.OrderSheetDto> orderList;
        public OrderListBySellerRespDto(List<OrderSheet> orders) {
            this.orderList = orders.stream().map(
                    OrderListRespDto.OrderSheetDto::new
            ).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class OrderSheetDto{
            private Long id;
            private String username; // 주문자
            private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
            private LocalDateTime createdAt;

            public OrderSheetDto(OrderSheet orderSheet) {
                this.id = orderSheet.getId();
                this.username = orderSheet.getUser().getUsername();
                this.totalPrice = orderSheet.getTotalPrice();
                this.createdAt = orderSheet.getCreatedAt();
            }
        }
    }
}
