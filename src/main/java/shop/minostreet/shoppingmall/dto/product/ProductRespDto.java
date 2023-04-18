package shop.minostreet.shoppingmall.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import shop.minostreet.shoppingmall.domain.Product;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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

        public ProductListRespDto(Page<Product> products) {
            this.productList = products.stream().map(
                    ProductDto::new
            ).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public static class ProductDto {
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

        @Getter
        @Setter
        public static class ProductUpdateRespDto {
            private String name;
            private Integer price;
            private Integer qty;

            public ProductUpdateRespDto(Product product) {
                this.name = product.getName();
                this.price = product.getPrice();
                this.qty = product.getQty();
            }
        }
    }
}
