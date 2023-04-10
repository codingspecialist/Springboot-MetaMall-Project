package shop.mtcoding.metamall.model.ordersheet;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.model.user.User;

import java.util.List;
import java.util.Optional;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {

    @Query("select distinct os from OrderSheet os left join fetch os.user u left join fetch os.orderProductList op left join fetch op.product p left join fetch p.seller s")
    List<OrderSheet> findAll();
    @Query("select distinct os from OrderSheet os left join fetch os.orderProductList op left join fetch op.product p left join fetch p.seller s where os.user=:user")
    List<OrderSheet> findByUser(@Param("user") User user);

    @Query("select distinct os from OrderSheet os left join fetch os.user u left join fetch os.orderProductList op left join fetch op.product p where p.seller=:seller")
    List<OrderSheet> findBySeller(@Param("seller") User seller);

    @Query("select os from OrderSheet os left join fetch os.orderProductList op left join fetch op.product p left join fetch p.seller s where os.id=:id and os.user=:user")
    Optional<OrderSheet> findByIdAndUser(@Param("id") Long id, @Param("user") User user);

    @Query("select os from OrderSheet os left join fetch os.user u left join fetch os.orderProductList op left join fetch op.product p left join fetch p.seller s where os.id=:id and s=:seller")
    Optional<OrderSheet> findByIdAndSeller(@Param("id") Long id, @Param("seller") User seller);
}
