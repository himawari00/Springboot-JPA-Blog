package com.jsj.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jsj.blog.model.RoleType;
import com.jsj.blog.model.User;
import com.jsj.blog.repository.UserRepository;

import jakarta.transaction.Transactional;


//html파일이 아니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired //의존성 주입(DI)
	private UserRepository userRepository;
	
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return "삭제 실패 :"+id;
		}
			//db에 없는 id 입력해도 오류가 안남 왜???????????????????????????????왜이럼??????????????
		
		return "삭제 : "+id;
	}
	
	//save함수는 id를 전달하지 않으면 insert를 해주고
	//save함수는 id를 전달하고 해당 id에 대한 데이터가 있으면 update --> @Transcational을 이용하면 save를 사용하지 않고 수정이 가능
	//save함수는 id를 전달하고 해당 id에 대한 데이터가 없으면 insert
	//email,password을 수정
	
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser ) {
		//json 데이터를 요청 -> Java Object(MessageConverter의 Jackson라이브러리가 변환해서 받음)
		
		System.out.println(id);
		System.out.println(requestUser.getPassword());
		System.out.println(requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정 실패");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//requestUser.setId(id);
		//userRepository.save(requestUser);
		
		//더티 체킹 
		return user;
	}
	
	//http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	
	//한페이지당 2건의 데이터를 리턴받아 볼 예정 
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);		
		List<User> users = pagingUser.getContent();		
		
		return users;
	}
	
	
	//{id}주소로 파라미터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		//user/4을 찾으면 user가 null되면 문제
		//optional로 user객체를 감싸서 가져옴 -> null인지판단해서 return해라
		
		//람다식
		User user =userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id : "+id);
			}
		});
		//요청 : 웹브라우저
		//user 객체 = 자바 오브젝트 
		//변환 (웹브라우저가 이해할 수 있는 데이터) -> json!!! (Gson 라이브러리)
		//스프링부트 -> MessageConverter라는 애가 응답시에 자동 작동
		//만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		//user 오브젝트를 json으로 변환해서 브라우저로 전달
		return user;
	}
	
	
	
	//http://localhost:8000/blog/dummy/join(요청)
	//http의 body에 username,password,email 데이터를 가지고 (요청)
	@PostMapping("/dummy/join")
	public String join(User user) { //key=value (약속된 규칙)
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getEmail());
		//String username, String password, String Email
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}

}
