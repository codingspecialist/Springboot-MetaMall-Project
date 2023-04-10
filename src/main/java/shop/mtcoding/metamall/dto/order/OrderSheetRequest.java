package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.user.User;


public class OrderSheetRequest {

    @Getter @Setter
    public static class OrderSheetSave{
        private User user;
        private Integer totalPrice;

        public OrderSheet toEntity(OrderSheetRequest.OrderSheetSave dto, User user){
            return OrderSheet.builder()
                    .user(user)
                    .totalPrice(dto.totalPrice)
                    .build();
        }
    }
}
