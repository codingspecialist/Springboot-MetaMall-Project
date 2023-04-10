package shop.mtcoding.metamall.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.metamall.domain.User;
import shop.mtcoding.metamall.domain.UserEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {
    @Getter @Setter
    public static class JoinReqDto {

        //영문, 숫자만 가능하고, 길이는 2~20자만 가능하도록, 공백도 불가능
        @Pattern(regexp = "^[A-Za-z0-9]{2,20}$",message = "영문/숫자 2~20자 이내로 작성해 주세요.")
        @NotEmpty
        private String username;

        //길이 4~20만 가능
        @Size(min=4, max=20) //String에만 사용가능한 어노테이션
        @NotEmpty
        private String password;
        //이메일 형식을 준수하도록
        @Pattern(regexp = "^[A-Za-z0-9]{2,10}@[A-Za-z0-9]{2,6}\\.[a-zA-Z]{2,3}$",message = "이메일 형식으로 작성해 주세요.")
        @NotEmpty
        private String email;

        //DTO를 엔티티로 변환하는 메서드 작성
        //: 패스워드 인코더를 파라미터로 받아서 패스워드 인코딩 수행
        public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder){
            return User.builder()
                    .username(username)
                    //패스워드는 인코딩 필요
//                    .password(password)
                    .password(bCryptPasswordEncoder.encode(password))
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginReqDto{
        private String username;
        private String password;
    }
}