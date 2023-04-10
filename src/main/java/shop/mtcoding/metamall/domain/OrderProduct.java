package shop.mtcoding.metamall.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

//Audit 기능 사용하기 위한 어노테이션 1
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Table(name = "order_product_tb")
@Entity
public class OrderProduct { // 주문 상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    private Integer count; // 상품 주문 개수
    private Integer orderPrice; // 상품 주문 금액

    @ManyToOne
    private OrderSheet orderSheet;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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
