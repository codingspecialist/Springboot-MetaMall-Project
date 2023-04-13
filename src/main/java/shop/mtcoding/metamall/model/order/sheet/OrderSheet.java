package shop.mtcoding.metamall.model.order.sheet;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.metamall.model.order.product.OrderProduct;
import shop.mtcoding.metamall.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter // DTO 만들면 삭제해야됨
@Getter
@Table(name = "order_sheet_tb")
@Entity
public class OrderSheet { // 주문서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // 주문자

    // 양방향 맵핑: OrderSheet에서 모든 것을 관리하겠다
    // checkpoint -> 무한참조
    @OneToMany(mappedBy = "orderSheet", cascade = CascadeType.ALL, orphanRemoval = true) // 이 부분 생각해보기
    private List<OrderProduct> orderProductList = new ArrayList<>(); // 총 주문 상품 리스트

    @Column(nullable = false)
    private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
        orderProduct.syncOrderSheet(this); // 내가 어느 주문서에 꼽혀있는지 알려주는 것
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProductList.remove(orderProduct);
        orderProduct.syncOrderSheet(null); // 주문에 등록이 되어있으면 안되니까
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // 연관관계 메서드 구현 필요
    @Builder
    public OrderSheet(Long id, User user, Integer totalPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
