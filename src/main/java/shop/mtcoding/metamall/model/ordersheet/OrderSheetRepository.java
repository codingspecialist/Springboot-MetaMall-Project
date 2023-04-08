package shop.mtcoding.metamall.model.ordersheet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
    List<OrderSheet> findByUserIdAndStatusNot(Long userId, OrderStatus status);

    List<OrderSheet> findByStatusNot(OrderStatus status);

}
