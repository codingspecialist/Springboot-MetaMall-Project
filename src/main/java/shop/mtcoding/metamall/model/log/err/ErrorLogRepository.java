package shop.mtcoding.metamall.model.log.err;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
