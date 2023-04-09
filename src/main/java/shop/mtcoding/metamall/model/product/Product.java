package shop.mtcoding.metamall.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name = "product_tb")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 상품 이름
    private Integer price; // 상품 가격
    private Integer qty; // 상품 재고
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(Product product){
        this.name = product.getName();
        this.price = product.getPrice();
        this.qty = product.getQty();
    }

    @Builder
    public Product(Long id, String name, Integer price, Integer qty, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
