package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	private final StackRepository stackRepository;

	/**
	 * Spring Security 필수 메소드 구현
	 *
	 * @param email 이메일
	 * @return UserDetails
	 * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
	 */
	@Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserInfo로 반환 타입 지정 (자동으로 다운 캐스팅됨)
	public User loadUserByUsername(String email) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException((email)));
	}
	/**
	 * 회원정보 저장
	 *
	 * @param infoDto 회원정보가 들어있는 DTO
	 * @return 회원번호 PK
	 */
	@Transactional
	public Long save(UserDto infoDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		infoDto.setPassword(encoder.encode(infoDto.getPassword()));
		LocalDate date = LocalDate.now();
		User user = userRepository.save(User.builder()
				.email(infoDto.getEmail())
				.auth(infoDto.getAuth())
				.name(infoDto.getName())
				.region(infoDto.getRegion())
				.job(infoDto.getJob())
				.career(infoDto.getCareer())
				.date(date.toString())
				.password(infoDto.getPassword()).build());
		System.out.println("-----스택 제대로 들어갔냐?");
		for(int i = 0; i < infoDto.getStacks().size(); i++) {
			System.out.println(infoDto.getStacks().get(i));
		}
		for(int i = 0; i < infoDto.getStacks().size(); i++) {
			user.addStacks(infoDto.getStacks().get(i));
		}
		return user.getUserIndex();
	}
	public Boolean isExistEmail(String email) {
	    return userRepository.existsByEmail(email);
	  }
}
