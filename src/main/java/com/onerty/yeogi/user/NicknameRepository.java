package com.onerty.yeogi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NicknameRepository extends JpaRepository<Nickname, Long> {
    List<Nickname> findByType(NicknameType type);
}