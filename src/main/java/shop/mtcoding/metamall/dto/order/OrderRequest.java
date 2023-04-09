package shop.mtcoding.metamall.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrderRequest {
    @Getter @Setter
    public static class OrderDto {
        @NotBlank(message = "입력은 필수입니다")
        private String name;
        @NotNull(message = "입력은 필수입니다")
        private Integer count;
    }
}
