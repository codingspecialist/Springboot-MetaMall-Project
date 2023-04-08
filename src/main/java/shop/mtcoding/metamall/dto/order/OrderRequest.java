package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.user.User;

public class OrderRequest {
    @Getter @Setter
    public static class OrderDto {
        private String name;
        private Integer count;
    }
}
