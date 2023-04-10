package shop.mtcoding.metamall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.metamall.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

//    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
