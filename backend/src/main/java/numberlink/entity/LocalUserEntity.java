package numberlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name="local_accounts")
@Entity(name="LocalUser")
public class LocalUserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    UserEntity user;

    @Column(name = "encoded_password", nullable = false)
    private String encodedPassword;
}
