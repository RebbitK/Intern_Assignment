package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.User;
import java.util.Optional;

public interface UserRepositoryCustom {

	Optional<User> findByUsername(String username);

	Optional<User> findByNickname(String nickname);
}
