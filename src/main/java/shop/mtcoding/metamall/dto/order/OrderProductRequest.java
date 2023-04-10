package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.product.Product;

public class OrderProductRequest {

    @Getter
    @Setter
    public static class OrderSaveDto{
        private Long productId;
        private Integer count;
        private Integer orderPrice;

        public OrderProduct toEntity(OrderProductRequest.OrderSaveDto dto, Product product){
            return OrderProduct.builder()
                    .product(product)
                    .count(dto.count)
                    .orderPrice(dto.orderPrice)
                    .build();
        }
    }
}
