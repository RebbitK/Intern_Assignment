package com.example.demo.domain.refresh.repository;

import com.example.demo.domain.refresh.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Optional<RefreshToken> findByUserId(Long userId);

}