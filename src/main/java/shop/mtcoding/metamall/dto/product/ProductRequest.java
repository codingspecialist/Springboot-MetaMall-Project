package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

public class ProductRequest {
    @Getter
    @Setter
    public static class RegisterDto {
        private String name;
        private Integer price;
        private Integer qty;
    }
}
