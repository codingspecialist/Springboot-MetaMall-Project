package shop.mtcoding.metamall.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.mtcoding.metamall.domain.OrderProduct;
import shop.mtcoding.metamall.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//Audit 기능 사용하기 위한 어노테이션 1
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Table(name = "order_sheet_tb")
@Entity
public class OrderSheet { // 주문서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user; // 주문자
    @OneToMany(mappedBy = "orderSheet")
    private List<OrderProduct> orderProductList = new ArrayList<>(); // 총 주문 상품 리스트
    private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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
