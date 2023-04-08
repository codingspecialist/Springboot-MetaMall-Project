package shop.mtcoding.metamall.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class OrderProductDto {

    private Long productId;
    private String productName;
    private Integer count;

    @Builder
    public OrderProductDto(Long id, String name, Integer count)
    {
        this.productId = id;
        this.productName = name;
        this.count = count;
    }
}
