package com.jsj.blog.model;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

//ORM -> JAVA Object -> 테이블로 매핑해주는 기술

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity //User클래스가 MySQL에 테이블이 생성이 된다.
//@DynamicInsert // insert시에 null인 필드를 제외
public class User {

	@Id //Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 시퀀스, auto_increment
	
	@Column(nullable =false,length =30)
	private String username; //아이디
	
	@Column(nullable =false,length =100)
	private String password;
	
	@Column(nullable =false,length =50)
	private String email;
	
	//@ColumnDefault("'User'") //"안에 ' <--문자라는거 알려줌
	//DB는 RoleType이 없다
	@Enumerated(EnumType.STRING)
	private RoleType role; //enum을 쓰는게좋다. //ADMIN, USER 
	
	@CreationTimestamp //시간 자동입력
	private Timestamp createDate;
}
