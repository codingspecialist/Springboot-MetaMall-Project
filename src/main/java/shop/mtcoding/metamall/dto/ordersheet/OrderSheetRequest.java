package shop.mtcoding.metamall.dto.ordersheet;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.user.User;

public class OrderSheetRequest {
    @Getter
    @Setter
    public static class OrderDto {
        private User user;
        private Integer totalPrice;

        public OrderSheet toEntity() {
            return OrderSheet.builder()
                    .user(user)
                    .totalPrice(totalPrice)
                    .build();
        }
    }
}
