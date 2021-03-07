package com.cos.myjpa.web.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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

import com.cos.myjpa.domain.post.Post;
import com.cos.myjpa.domain.post.PostRepository;
import com.cos.myjpa.domain.user.User;
import com.cos.myjpa.domain.user.UserRepository;
import com.cos.myjpa.web.post.dto.PostSaveReqDto;
import com.cos.myjpa.web.post.dto.PostUpdateReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate(); // increment를
																											// 초기화
		entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}

	@Test
	public void save_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		User user = new User(null, "ssar", "1234", "ssar@nate.com", LocalDateTime.now(), null);
		userRepository.save(user);

		PostSaveReqDto dto = new PostSaveReqDto();
		dto.setTitle("제목1");
		dto.setContent("내용1");
		String content = new ObjectMapper().writeValueAsString(dto); // json 데이터

		// when (테스트 실행)
		ResultActions resultAction = mockMvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON_UTF8)
				.sessionAttr("principal", user).content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then (검증)
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.title").value("제목1"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void findById_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		User principal = new User();
		principal.setUsername("ssar");
		principal.setPassword(null);
		principal.setEmail("ssar@nate.com");
		userRepository.save(principal);

		List<Post> posts = new ArrayList<>();
		posts.add(new Post(null, "제목1", "내용1", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목2", "내용2", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목3", "내용3", principal, LocalDateTime.now()));
		postRepository.saveAll(posts);

		Long id = 1L;
		// when
		ResultActions resultAction = mockMvc.perform(get("/post/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.title").value("제목1"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findAll_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		User principal = new User();
		principal.setUsername("ssar");
		principal.setPassword(null);
		principal.setEmail("ssar@nate.com");
		userRepository.save(principal);

		List<Post> posts = new ArrayList<>();
		posts.add(new Post(null, "제목1", "내용1", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목2", "내용2", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목3", "내용3", principal, LocalDateTime.now()));
		postRepository.saveAll(posts);

		// when
		ResultActions resultAction = mockMvc.perform(get("/post").accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.[0].title").value("제목1"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void updateById_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		User principal = new User();
		principal.setUsername("ssar");
		principal.setPassword(null);
		principal.setEmail("ssar@nate.com");
		userRepository.save(principal);

		List<Post> posts = new ArrayList<>();
		posts.add(new Post(null, "제목1", "내용1", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목2", "내용2", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목3", "내용3", principal, LocalDateTime.now()));
		postRepository.saveAll(posts);

		Long id = 1L;
		PostUpdateReqDto dto = new PostUpdateReqDto();
		dto.setTitle("제목수정1");
		dto.setContent("내용수정1");
		String content = new ObjectMapper().writeValueAsString(dto);

		// when
		ResultActions resultAction = mockMvc.perform(put("/post/{id}", id).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.*.title").value("제목수정1"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void deleteById_테스트() throws Exception {
		// given (테스트를 하기 위한 준비)
		User principal = new User();
		principal.setUsername("ssar");
		principal.setPassword(null);
		principal.setEmail("ssar@nate.com");
		userRepository.save(principal);

		List<Post> posts = new ArrayList<>();
		posts.add(new Post(null, "제목1", "내용1", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목2", "내용2", principal, LocalDateTime.now()));
		posts.add(new Post(null, "제목3", "내용3", principal, LocalDateTime.now()));
		postRepository.saveAll(posts);

		Long id = 1L;

		// when
		ResultActions resultAction = mockMvc.perform(delete("/post/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());


	}
}
