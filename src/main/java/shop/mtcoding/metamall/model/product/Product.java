package shop.mtcoding.metamall.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "product_tb")
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 상품 이름
    private Integer price; // 상품 가격
    private Integer qty; // 상품 재고
    @ManyToOne(fetch = FetchType.LAZY)
    private User seller;
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
    public Product(Long id, String name, Integer price, Integer qty, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.seller = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(ProductRequest.ProductDto dto){
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.qty = dto.getQty();
    }
}
