package com.cos.myjpa.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.myjpa.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class User { // User 1 <-> Post N
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Table, auto_increment, Sequence
	private Long id;
	private String username;
	private String password;
	private String email;
	@CreationTimestamp // 자동으로 현재시간이 들어감.
	private LocalDateTime createDate;
	
	// 역방향 매핑
	@JsonIgnoreProperties({"user"}) // Post 안에 있는 user를 getter 때리지 마라.
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // 나는 FK의 주인이 아니다. FK는 user 변수이다.
	private List<Post> post;
	
	// User만 보고 싶을 때는 dto를 사용, 연관관계에 있는 Post 안의 user를 보고 싶지 않을 때는 JsonIgnore 사용
	
//	@Transient // DB에는 영향을 미치지 않게끔
//	private int value;
}
