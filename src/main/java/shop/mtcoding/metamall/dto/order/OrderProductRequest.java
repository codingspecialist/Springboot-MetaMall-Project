package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;

public class OrderProductRequest {
    @Getter
    @Setter
    public static class OrderDto {
        private Product product;
        private Integer count;
    }
}