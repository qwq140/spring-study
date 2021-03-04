package com.cos.myjpa.domain.post;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.myjpa.domain.user.User;
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
public class Post {
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Table, auto_increment, Sequence
	private Long id;
	@Column(length = 60, nullable = false)
	private String title;
	@Lob // 대용량 데이터
	private String content;
	
	@ManyToOne(fetch = FetchType.EAGER) // 연관관계 맺는 법. FK의 주인인 곳에서 적어야 됨.
	@JoinColumn(name="userId")
	@JsonIgnoreProperties({"posts"})
	private User user;
	
	@CreationTimestamp // 자동으로 현재시간이 들어감.
	private LocalDateTime createDate;
}
