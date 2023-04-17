package shop.mtcoding.metamall.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.metamall.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter // DTO 만들면 삭제해야됨
@Getter
@Table(name = "product_tb")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User seller; // seller_id
    @Column(nullable = false, length = 50)
    private String name; // 상품 이름
    @Column(nullable = false)
    private Integer price; // 상품 가격
    @Column(nullable = false)
    private Integer qty; // 상품 재고
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 판매자가 상품을 등록 후 변경 가능
    // setter 사용금지
    public void update(String name, Integer price, Integer qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    // 주문 시 재고량 check(구매자)
    // 객체의 상태를 변경하는 것은 의미있는 메소드로(setter 사용금지)
    public void updateQty(Integer orderCount) {
        if(this.qty < orderCount) {
            // 주문수량이 재고수량을 초과했습니다.
        }
    }

    // 주문 취소 (재고수량 변경 - rollback)
    public void rollbackQty(Integer orderCount) {
        this.qty += orderCount;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Product(Long id, User seller, String name, Integer price, Integer qty, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
