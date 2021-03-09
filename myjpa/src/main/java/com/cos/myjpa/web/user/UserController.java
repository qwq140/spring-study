package com.cos.myjpa.web.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.myjpa.domain.post.Post;
import com.cos.myjpa.domain.user.User;
import com.cos.myjpa.domain.user.UserRepository;
import com.cos.myjpa.service.UserService;
import com.cos.myjpa.web.dto.CommonRespDto;
import com.cos.myjpa.web.post.dto.PostRespDto;
import com.cos.myjpa.web.user.dto.UserJoinReqDto;
import com.cos.myjpa.web.user.dto.UserLoginReqDto;
import com.cos.myjpa.web.user.dto.UserRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {
	
	private final UserRepository userRepository;
	private final HttpSession session;
	private final UserService userService;
	
	// 인증만 필요
	@GetMapping("/user")
	public CommonRespDto<?> findAll(){
		
//		usersEntity.stream().forEach((u)->{
//			userRespDtos.add(new UserRespDto(u));
//		});
		
//		List<UserRespDto> userRespDtos = usersEntity.stream().map((u)->{
//			return new UserRespDto(u);
//		}).collect(Collectors.toList());
		
		return new CommonRespDto<>(1,"성공",userService.전체찾기());
	}
	
	// 인증만 필요
	@GetMapping("/user/{id}") 
	public CommonRespDto<?> findById(@PathVariable Long id){
		
		return new CommonRespDto<>(1,"성공",userService.한건찾기(id));
	}
	
	// 인증만 필요
	@GetMapping("/user/{id}/post") 
	public CommonRespDto<?> profile(@PathVariable Long id){
		
		return new CommonRespDto<>(1,"성공",userService.프로파일(id));
	}
	
	// 인증도 필요없음
	@PostMapping("/join") //auth(인증)
	public CommonRespDto<?> join(@RequestBody UserJoinReqDto userJoinReqDto){
		
		return new CommonRespDto<>(1,"성공",userService.회원가입(userJoinReqDto));
	}
	
	// 인증도 필요없음
	@PostMapping("/login")
	public CommonRespDto<?> login(@RequestBody UserLoginReqDto userLoginReqDto){
		
		User userEntity = userService.로그인(userLoginReqDto);
		
		if(userEntity == null) {
			return new CommonRespDto<>(-1,"실패",null);
		} else {
			userEntity.setPassword(null);
			session.setAttribute("principal", userEntity);
			return new CommonRespDto<>(1,"성공",userEntity);
		}
	}
}
