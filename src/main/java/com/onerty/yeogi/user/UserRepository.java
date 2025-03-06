package com.onerty.yeogi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserIdentifier(String email);

    boolean existsByNickname(String nickname);
}