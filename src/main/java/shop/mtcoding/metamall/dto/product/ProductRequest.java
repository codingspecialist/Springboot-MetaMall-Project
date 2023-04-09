package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ProductRequest {

    @Getter @Setter
    public static class SaveDTO {
        @NotEmpty
        private String name;

        // 숫자는 NotNull로 검증한다
        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer price;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer qty;

        public Product toEntity(){
            return Product.builder()
                    .name(name)
                    .price(price)
                    .qty(qty)
                    .build();
        }
    }

    @Getter @Setter
    public static class UpdateDTO {
        @NotEmpty
        private String name;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer price;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer qty;
    }
}
