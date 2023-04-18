package shop.minostreet.shoppingmall.dto.product;

import lombok.Getter;
import lombok.Setter;
import shop.minostreet.shoppingmall.domain.Product;
import shop.minostreet.shoppingmall.domain.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ProductReqDto {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String name; // 상품 이름
//    private Integer price; // 상품 가격
//    private Integer qty; // 상품 재고
    @Getter
    @Setter
    public static class ProductRegisterReqDto {
        //한글, 영문, 숫자만 가능하고, 길이는 2~20자만 가능하도록, 공백도 불가능
        @NotEmpty
        private String name;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        private Integer price;

        @Digits(fraction = 0, integer = 9)
        @NotNull        //최소 1개부터 ~ 9999개까지
        private Integer qty;

        public Product toEntity(User user) {
            return Product.builder()
                    .seller(user)
                    .name(name)
                    .price(price)
                    .qty(qty)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ProductUpdateReqDto {
        //한글, 영문, 숫자만 가능하고, 길이는 2~20자만 가능하도록, 공백도 불가능
        @Pattern(regexp = "^[ㄱ-힣A-Za-z0-9]{2,20}$", message = "한글/영문/숫자 2~20자 이내로 작성해 주세요.")
        @NotEmpty
        private String name;

        @Digits(fraction = 0, integer = 9)
        @NotNull    //: 숫자의 길이 체크 최소 3자 최대 8자
        //최소 100원부터 ~ 9000만원까지
        private Integer price;

        @Digits(fraction = 0, integer = 9)
        @NotNull
        //최소 1개부터 ~ 9999개까지
        private Integer qty;

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .price(price)
                    .qty(qty)
                    .build();
        }
    }
}
