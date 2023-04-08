package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductRequest
{
    private Long productId;
    private Integer count;
}
