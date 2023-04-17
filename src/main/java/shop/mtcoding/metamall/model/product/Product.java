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
    private User seller;
    @Column(nullable = false, length = 50)
    private String name; // 상품 이름
    @Column(nullable = false)
    private Integer price; // 상품 가격
    @Column(nullable = false)
    private Integer qty; // 상품 재고
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    /**
     상품 변경 (판매자)
     */
    public void update(String name, Integer price, Integer qty){
        this.name=name;
        this.price=price;
        this.qty=qty;
    }
    /**
     주문시 재고 변경(구매자)
     */
    public void updateQty(Integer orderCount){
        if(this.qty<orderCount){
            // checkpoint 주문수량이 재고수량을 초과하였습니다.

        }
        this.qty=this.qty-orderCount;
    }
    /**
     주문취소시 재고 변경(구매자+판매자)
     */
    public void rollbackQty(Integer orderCount){
        this.qty=this.qty+orderCount;
    }
}
