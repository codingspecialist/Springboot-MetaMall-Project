package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class ProductRequest {

    @Getter @Setter
    public static class ProductDto {
        @NotNull
        private String name;
        @NotNull
        private Integer price;
        @NotNull
        private Integer quantity;
    }
}
