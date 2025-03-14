package com.onerty.yeogi.customer.user;


import com.onerty.yeogi.common.user.UserRole;
import com.onerty.yeogi.common.util.BaseEntity;
import com.onerty.yeogi.customer.user.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // 명시적으로 컬럼명을 설정
    private Long userId;

    @Column(name = "user_type")
    private int userType;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "user_password")
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public User(UserSignupRequest dto) {
        this.userType = dto.signupType();
        this.nickname = dto.nick();
        this.phoneNumber = dto.phoneNumber();
        this.gender = dto.gender();
        this.birthDate = dto.birth();
        this.userIdentifier = dto.uid();
        this.userPassword = dto.upw();
    }
}
