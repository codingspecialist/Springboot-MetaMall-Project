package shop.mtcoding.metamall.model.ordersheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {

    @Query("SELECT os FROM OrderSheet os " +
            "JOIN FETCH os.orderProductList join fetch os.user " +
            "WHERE os.user.username = :username")
    public Optional<List<OrderSheet>> findOrdData(@Param("username") String username);


    @Query("SELECT os FROM OrderSheet os " +
            "JOIN FETCH os.orderProductList JOIN FETCH os.user")
    public Optional<List<OrderSheet>> findAllData();


    @Query("SELECT os FROM OrderSheet os JOIN FETCH os.orderProductList JOIN FETCH os.user WHERE os.id  = :id ")
    public Optional<OrderSheet> findByIdWithUser(@Param("id") Long id);
}
