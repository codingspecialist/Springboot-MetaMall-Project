package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;

public class ProductRequest {
    @Getter
    @Setter
    public static class saveDTO {
        private String name;
        private Integer price;
        private Integer qty;

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .price(price)
                    .qty(qty)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class updateDTO {
        private String name;
        private Integer price;
        private Integer qty;
    }
}
