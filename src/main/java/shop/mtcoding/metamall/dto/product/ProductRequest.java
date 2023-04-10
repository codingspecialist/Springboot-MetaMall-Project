package shop.mtcoding.metamall.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;

public class ProductRequest {

    @Getter @Setter
    public static class SaveDto {
        private String name;
        private Integer price;
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
    public static class UpdateDto {
        private Long id;
        private String name;
        private Integer price;
        private Integer qty;

        public Product toEntity(ProductRequest.UpdateDto dto){
            return Product.builder()
                    .name(dto.name)
                    .price(dto.price)
                    .qty(dto.qty)
                    .build();
        }
    }
}
