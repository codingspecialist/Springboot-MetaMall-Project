package shop.mtcoding.metamall.model.ordersheet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter // DTO 만들면 삭제해야됨
@Getter
@Builder
@AllArgsConstructor
@Table(name = "order_sheet_tb")
@Entity
public class OrderSheet { // 주문서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username",insertable = false,updatable = false)
    private User user; // 주문자


    @Builder.Default
    @OneToMany(mappedBy = "orderSheet",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>(); // 총 주문 상품 리스트
    private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Column(name = "username")
    private String userId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addOrderProductList(List<OrderProduct> orderProductList){
            orderProductList.forEach(orderProduct -> orderProduct.setOrderSheet(this));

            this.orderProductList = orderProductList;
    }
    
    // 연관관계 메서드 구현 필요


}
