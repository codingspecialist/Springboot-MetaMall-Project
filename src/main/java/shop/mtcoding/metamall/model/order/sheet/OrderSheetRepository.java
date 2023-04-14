package shop.mtcoding.metamall.model.order.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
    @Query("select os from OrderSheet os where os.user.id = :userId")
    List<OrderSheet> findByUserId(@Param("userId") Long userId);
}
