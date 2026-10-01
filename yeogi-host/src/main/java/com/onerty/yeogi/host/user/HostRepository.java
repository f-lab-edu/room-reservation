package com.onerty.yeogi.host.user;

import com.onerty.yeogi.common.user.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}

