package shop.mtcoding.metamall.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
@Table(name="user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String role; // USER(고객), SELLER(판매자), ADMIN(관리자)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy ="user", fetch = FetchType.LAZY)
    private List<OrderSheet> orderSheet = new ArrayList<>();

    @PrePersist //insert직전에 발동
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate //update할때 발동
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public User(Long id, String username, String password, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }
}
