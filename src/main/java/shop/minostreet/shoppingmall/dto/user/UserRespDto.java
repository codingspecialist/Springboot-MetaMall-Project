package shop.minostreet.shoppingmall.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.minostreet.shoppingmall.domain.User;
import shop.minostreet.shoppingmall.util.MyDateUtil;

// 응답 DTO는 서비스 배우고 나서 하기 (할 수 있으면 해보기)
public class UserRespDto {
    @ToString
    @Getter
    @Setter
    //임시로 서비스 안에 회원가입 응답을 위한 DTO 작성
    public static class JoinRespDto{
        private Long id;
        private String username;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Getter
    @Setter
    public static class LoginRespDto{
        private Long id;
        private String username;
        private String createdAt;

        public LoginRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            //String으로 응답하기 위해 LocalDateTime을 변환하는 유틸클래스 작성
//            this.createdAt = user.getCreatedAt();
            this.createdAt = MyDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

}
