package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.order.product.OrderProduct;
import shop.mtcoding.metamall.model.product.Product;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    @Getter
    @Setter
    public static class SaveDTO {

        private List<OrderProductDTO> orderProducts;
        @Getter
        @Setter
        public static class OrderProductDTO {
            private Long productId;
            private Integer count;
        }
        public List<Long> getIds() {
            return orderProducts.stream().map((orderProduct) ->
                    orderProduct.getProductId()).collect(Collectors.toList());
        }

        public List<OrderProduct> toEntity(List<Product> products) {

            return orderProducts.stream()
                    .flatMap((orderProduct) -> {
                                Long productId = orderProduct.productId;
                                Integer count = orderProduct.getCount();
                                return products.stream()
                                        .filter((product) ->
                                                product.getId().equals(productId))
                                        .map((product) -> OrderProduct.builder()
                                                .product(product)
                                                .count(count)
                                                .orderPrice(product.getPrice() *
                                                        count)
                                                .build());
                            }
                    ).collect(Collectors.toList());
        }
    }
}