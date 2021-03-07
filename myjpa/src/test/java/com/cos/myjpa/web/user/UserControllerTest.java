package com.cos.myjpa.web.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.myjpa.domain.user.User;
import com.cos.myjpa.domain.user.UserRepository;
import com.cos.myjpa.web.user.dto.UserJoinReqDto;
import com.cos.myjpa.web.user.dto.UserLoginReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate(); // increment를
																											// 초기화
	}

	@Test
	public void findAll_테스트() throws Exception {
		// given
		List<User> users = new ArrayList<>();
		users.add(new User(null, "ssar", "1234", "ssar@nate.com", LocalDateTime.now(), null));
		users.add(new User(null, "cos", "1234", "cos@nate.com", LocalDateTime.now(), null));
		users.add(new User(null, "love", "1234", "love@nate.com", LocalDateTime.now(), null));
		userRepository.saveAll(users);

		// when
		ResultActions resultAction = mockMvc.perform(get("/user").accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.*.[0].username").value("ssar")).andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_테스트() throws Exception {
		// given
		List<User> users = new ArrayList<>();
		users.add(new User(null, "ssar", "1234", "ssar@nate.com", LocalDateTime.now(), null));
		users.add(new User(null, "cos", "1234", "cos@nate.com", LocalDateTime.now(), null));
		userRepository.saveAll(users);

		Long id = 1L;

		// when
		ResultActions resultAction = mockMvc.perform(get("/user/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.username").value("ssar"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void profile_테스트() throws Exception {
		// given
		List<User> users = new ArrayList<>();
		users.add(new User(null, "ssar", "1234", "ssar@nate.com", LocalDateTime.now(), null));
		users.add(new User(null, "cos", "1234", "cos@nate.com", LocalDateTime.now(), null));
		userRepository.saveAll(users);

		Long id = 1L;

		// when
		ResultActions resultAction = mockMvc
				.perform(get("/user/{id}/post", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.username").value("ssar"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void join_테스트() throws Exception {
		// given
		UserJoinReqDto dto = new UserJoinReqDto();
		dto.setUsername("ssar");
		dto.setPassword("1234");
		dto.setEmail("ssar@nate.com");
		String content = new ObjectMapper().writeValueAsString(dto); // json 데이터

		// when
		ResultActions resultAction = mockMvc.perform(post("/join")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*.username").value("ssar"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void login_테스트() throws Exception {
		//given
		User user = new User(null, "ssar", "1234", "ssar@nate.com", LocalDateTime.now(), null);
		userRepository.save(user);
		
		UserLoginReqDto dto = new UserLoginReqDto();
		dto.setUsername("ssar");
		dto.setPassword("1234");
		String content = new ObjectMapper().writeValueAsString(dto); // json 데이터

		// when
		ResultActions resultAction = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*.username").value("ssar"))
				.andDo(MockMvcResultHandlers.print());
	}
}
