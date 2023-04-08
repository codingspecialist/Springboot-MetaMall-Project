package shop.mtcoding.metamall.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.metamall.dto.product.OrderProductDto;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class OrderSheetDto {

    private Long id;
    private String username;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderProductDto> orderProducts;

    @Builder
    public OrderSheetDto(OrderSheet orderSheet) {
        this.id = orderSheet.getId();
        this.username = orderSheet.getUser().getUsername();
        this.totalPrice = orderSheet.getTotalPrice();
        this.createdAt = orderSheet.getCreatedAt();
        this.status = orderSheet.getStatus();
        this.orderProducts = orderSheet.getOrderProductList().stream()
                .map(orderProduct -> new OrderProductDto(
                        orderProduct.getProduct().getId(),
                        orderProduct.getProduct().getName(),
                        orderProduct.getCount()))
                .collect(Collectors.toList());
    }

}
