package com.onerty.yeogi.user;


import com.onerty.yeogi.user.dto.UserSignupRequest;
import com.onerty.yeogi.util.BaseEntity;
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

    @Column(name = "affiliate_user_id")
    private String affiliateUserId;

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "user_password")
    private String userPassword;

    public User(UserSignupRequest dto) {
        this.userType = dto.utype();
        this.nickname = dto.unick();
        this.phoneNumber = dto.phoneNumber();
        this.gender = dto.ugender();
        this.birthDate = dto.ubirth();
        this.affiliateUserId = dto.afUserId();
        this.userIdentifier = dto.uid();
        this.userPassword = dto.upw();
    }
}
