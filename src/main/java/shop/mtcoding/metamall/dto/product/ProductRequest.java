package shop.mtcoding.metamall.dto.product;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class ProductRequest {
    @Getter
    public static class ProductDto{
        @NotNull
        private String name;
        @NotNull
        private int price;
        @NotNull
        private int qty;

        @Builder
        public ProductDto(String name, int price, int qty) {
            this.name = name;
            this.price = price;
            this.qty = qty;
        }
    }
}
