package com.example.demo.repository;

import com.example.demo.config.TestConfig;
import com.example.demo.domain.user.dto.SignRequestDto;
import com.example.demo.domain.user.entity.RoleEnum;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	private User testUser(){
		String userName = "testUser";
		String password = "testPassword";
		String nickName = "testNickName";
		SignRequestDto requestDto = new SignRequestDto(userName, password, nickName);
		return new User(requestDto,password);
	}

	@Test
	@DisplayName("username으로 검색")
	void findByUsername(){
		//given
		User user = testUser();
		userRepository.save(user);
		//when
		Optional<User> testUser = userRepository.findByUsername(user.getUsername());
		//then
		assertEquals(user,testUser.get());

	}

	@Test
	@DisplayName("nickname으로 검색")
	void findByNickname(){
		//given
		User user = testUser();
		userRepository.save(user);
		//when
		Optional<User> testUser = userRepository.findByNickname(user.getNickname());
		//then
		assertEquals(user,testUser.get());

	}

	@Test
	@DisplayName("save 테스트")
	void save(){
		//given
		User user = testUser();
		//when
		User savedUser = userRepository.save(user);
		//then
		assertEquals(user,savedUser);
	}

	@Test
	@DisplayName("delete 테스트")
	void delete(){
		//given
		User user = testUser();
		userRepository.save(user);
		//when
		userRepository.delete(user);
		//then
		assertTrue(userRepository.findById(user.getId()).isEmpty());
	}

}
