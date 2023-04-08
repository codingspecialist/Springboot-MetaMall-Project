package shop.mtcoding.metamall.model.log.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrLogService {

    private final ErrorLogRepository errorLogRepository;

    public String log(Long userId, String message) {

        ErrorLog errorLog = ErrorLog.builder()
                .msg(message)
                .userId(userId)
                .build();
        return errorLogRepository.save(errorLog).getMsg();
    }
}
