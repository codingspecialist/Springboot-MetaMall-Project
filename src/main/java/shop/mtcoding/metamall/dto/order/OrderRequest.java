package shop.mtcoding.metamall.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderRequest {
    @Getter
    @NoArgsConstructor
    public static class OrderDto{
        private List<OrderProductDto> products;

        public OrderDto(List<OrderProductDto> products) {
            this.products = products;
        }
    }
    @Getter
    public static class OrderProductDto{
        @JsonProperty("product_id")
        private Long productId;
        private int count;

        @Builder
        public OrderProductDto(Long productId, int count) {
            this.productId = productId;
            this.count = count;
        }
    }
}
