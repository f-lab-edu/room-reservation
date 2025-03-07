package com.onerty.yeogi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserIdentifier(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByUserIdentifier(String userIdentifier);
}