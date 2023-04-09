package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class ProductRequest {
    @Getter @Setter
    public static class ProductDto {

        @NotBlank(message = " 입력은 필수입니다")
        private String productname;
    }
}
