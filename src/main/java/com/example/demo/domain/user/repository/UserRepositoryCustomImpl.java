package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.QUser;
import com.example.demo.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<User> findByUsername(String username) {
		User query = jpaQueryFactory.select(QUser.user)
			.from(QUser.user)
			.where(
				usernameEq(username),
				QUser.user.deletedAt.isNull()
			)
			.fetchOne();
		return Optional.ofNullable(query);
	}

	private BooleanExpression usernameEq(String username) {
		return Objects.nonNull(username) ? QUser.user.username.eq(username) : null;
	}

}
