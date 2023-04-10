package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRespDto {
    @Getter
    @Setter
    public static class ProductRegisterRespDto{
            private Long id;
            private String name;
            private Integer price;
            private Integer qty;

        public ProductRegisterRespDto(Product product) {
            this.id=product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
        }
    }

    @Getter
    @Setter
    public static class ProductListRespDto{
        private List<ProductDto> productList;

        public ProductListRespDto(List<Product> products) {
            this.productList = products.stream().map(
                    ProductDto::new
            ).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class ProductDto {
            private Long id;
            private String name;
            private Integer price;
            private Integer qty;

            public ProductDto(Product product) {
                this.id=product.getId();
                this.name = product.getName();
                this.price = product.getPrice();
                this.qty = product.getQty();
            }
        }
    }
}
