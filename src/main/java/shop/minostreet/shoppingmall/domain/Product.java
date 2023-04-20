package shop.minostreet.shoppingmall.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.minostreet.shoppingmall.dto.product.ProductReqDto.ProductUpdateReqDto;
import shop.minostreet.shoppingmall.handler.exception.MyApiException;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)

@NoArgsConstructor
@Setter // DTO 만들면 삭제해야됨
@Getter
@Table(name = "product_tb")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    private User seller;
    @Column(unique = false, length =20)
    private String name; // 상품 이름
    @Column(nullable = false)
    private Integer price; // 상품 가격

    @Column(unique = false, length =20)
    private Integer qty; // 상품 재고
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //Product와 관련된 메서드 작성 - 객체 상태 변경

    //상품 변경 메서드 (판매자만 가능)
    public void update(ProductUpdateReqDto productUpdateReqDto){
        this.name=productUpdateReqDto.getName();
        this.price=productUpdateReqDto.getPrice();
        this.qty=productUpdateReqDto.getQty();
    }

    //상품 주문시 재고 변경하는 메서드 (구매자가 호출)
    public void updateQty(Integer orderCount){
        if(this.qty<orderCount){
            throw new MyApiException("주문수량이 재고 수량을 초과했습니다.");
        }
        this.qty=this.qty-orderCount;
    }

    //주문 취소 재고 변경 (구매자, 판매자)
    public void rollbackQty(Integer orderCount){
        this.qty=this.qty+orderCount;
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
