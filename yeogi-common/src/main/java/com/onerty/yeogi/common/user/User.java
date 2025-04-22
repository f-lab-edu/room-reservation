package com.onerty.yeogi.common.user;


import com.onerty.yeogi.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
