package com.cos.myjpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.myjpa.domain.user.User;
import com.cos.myjpa.domain.user.UserRepository;
import com.cos.myjpa.web.user.dto.UserJoinReqDto;
import com.cos.myjpa.web.user.dto.UserLoginReqDto;
import com.cos.myjpa.web.user.dto.UserRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<UserRespDto> 전체찾기() {
		List<User> usersEntity = userRepository.findAll();
		List<UserRespDto> userRespDtos = new ArrayList<>();

		for (User user : usersEntity) {
			userRespDtos.add(new UserRespDto(user));
		}
		
		return userRespDtos;
	}

	@Transactional(readOnly = true)
	public UserRespDto 한건찾기(Long id) {
		User userEntity = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("id를 찾을 수 없습니다.");
		});
		
		UserRespDto userRespDto = new UserRespDto(userEntity); // 응답 dto는 필요할 때만 사용
		
		return userRespDto;
	}

	@Transactional(readOnly = true)
	public User 프로파일(Long id) {
		User userEntity = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("id를 찾을 수 없습니다.");
		});
		
		return userEntity;

	}

	// 쓰기는 트랜잭션을 발동시켜야 함. 다른쪽에서 동시에 접근하지 못하게 하기위해
	@Transactional
	public User 회원가입(UserJoinReqDto userJoinReqDto) { 
		User userEntity = userRepository.save(userJoinReqDto.toEntity());
		return userEntity;
	}
	
	@Transactional(readOnly = true)
	public User 로그인(UserLoginReqDto userLoginReqDto) {
		User userEntity = userRepository.findByUsernameAndPassword(userLoginReqDto.getUsername(), userLoginReqDto.getPassword());
		return userEntity;
	}
}
