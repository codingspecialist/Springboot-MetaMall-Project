package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.metamall.domain.OrderSheet;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
}
