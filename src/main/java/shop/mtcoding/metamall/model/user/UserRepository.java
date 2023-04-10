package shop.mtcoding.metamall.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Long id);

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}