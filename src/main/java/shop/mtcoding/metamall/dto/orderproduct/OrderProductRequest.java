package shop.mtcoding.metamall.dto.orderproduct;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.product.Product;

public class OrderProductRequest {
    @Getter
    @Setter
    public static class OrderDto {
        private Product product;
        private Integer count;
        private Integer orderPrice;

        public OrderProduct toEntity() {
            return OrderProduct.builder()
                    .product(product)
                    .count(count)
                    .orderPrice(orderPrice)
                    .build();
        }
    }
}
