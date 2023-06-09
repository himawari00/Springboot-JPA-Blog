package com.jsj.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter
//@Setter
@Data //<-- Getter & Setter
@NoArgsConstructor
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		//@AllArgsConstructor <--생성자
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}  
	
	
}
