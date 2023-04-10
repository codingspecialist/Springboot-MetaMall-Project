package shop.mtcoding.metamall.model.ordersheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.model.user.User;

import java.util.List;
import java.util.Optional;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {

    @Query("select os from OrderSheet os where os.user.id = :userId")
    Optional<OrderSheet> findByUserId(@Param("userId") Long userId);

    @Query("select os from OrderSheet os where os.user.id = :userId and os.id = :orderId")
    Optional<OrderSheet> findByUserIdAndOrderId(@Param("userId") Long userId,
                                                @Param("orderId") Long orderId);
}
