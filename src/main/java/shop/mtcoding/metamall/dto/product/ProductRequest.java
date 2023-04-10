package shop.mtcoding.metamall.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductRequest {
	@Getter
	@Setter
	public static class ProductSaveDto{
		private String name;
		private Integer price;
		private Integer qty;
	}
	@Getter
	@Setter
	public static class ProductUpdateDto{
		private String name;
		private Integer price;
		private Integer qty;
	}
}
