package com.example.config;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
		name="user",
		uniqueConstraints={
			@UniqueConstraint(
				columnNames={"email","logintype"}
			)
		}
	)
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long userIndex;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "logintype")
    private String logintype;

    @Column(name = "password")
    private String password;

    @Column(name = "auth")
    private String auth;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "region")
    private String region;
    
    @Column(name = "job")
    private String job;
    
    @OneToMany(targetEntity = UserStacks.class, mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserStacks> stacks = new ArrayList<>();
    
    @Column(name = "career")
    private String career;
    
    @Column(name = "date")
    private String date;
    
    public void addStacks(String stack) {
    	System.out.println("------얜 메소드내부임!");
    	System.out.println(stack);
    	this.stacks.add(UserStacks.builder()
    			.stack(stack)
    			.user(this) 
    			.build());
    }
    @Builder
    public User(String email, String password, String auth, String name, String region,String logintype, String job, String career, String date) {
    	this.email = email;
        this.logintype = logintype;
        this.password = password;
        this.auth = auth;
        this.name = name;
        this.region = region;
        this.job = job;
        this.career = career;
        this.date = date;
    }

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return email;
    }
    
    public String getLoginType() {
        return logintype;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true; // true -> 사용 가능
    }
}
