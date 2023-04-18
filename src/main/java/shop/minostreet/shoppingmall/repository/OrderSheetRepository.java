package shop.minostreet.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.minostreet.shoppingmall.domain.OrderSheet;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
}
