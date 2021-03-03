package com.cos.myjpa.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonRespDto<T> {
	private int statusCode; // 1은 정상 -1은 실패
	private String msg; // 오류 내용
	private T data;
}
