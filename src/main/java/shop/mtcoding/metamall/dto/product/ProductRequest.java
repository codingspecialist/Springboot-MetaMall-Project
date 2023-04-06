package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

public class ProductRequest {
    @Getter @Setter
    public static class ProductDto {
        private String name;
    }
}
