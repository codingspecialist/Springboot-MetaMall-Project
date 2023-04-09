package shop.mtcoding.metamall.model.orderproduct;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name = "order_product_tb")
@Entity
public class OrderProduct { // 주문 상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private Integer count; // 상품 주문 개수
    private Integer orderPrice; // 상품 주문 금액
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderSheet orderSheet;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public OrderProduct(Long id, Product product, Integer count, Integer orderPrice, LocalDateTime createdAt, LocalDateTime updatedAt, OrderSheet orderSheet) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.orderPrice = orderPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderSheet = orderSheet;
    }
}
