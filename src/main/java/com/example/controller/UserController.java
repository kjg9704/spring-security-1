package com.example.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


import com.example.config.JwtRequest;
import com.example.config.JwtResponse;
import com.example.config.JwtTokenUtil;
import com.example.config.JwtUserDetailsService;
import com.example.config.Response;
import com.example.config.UserDto;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(description = "로그인/회원가입 컨트롤러")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtUserDetailsService userService;
    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@ApiOperation(value = "로그인")
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "카카오 로그인 검증")
	@GetMapping(value = "/kakaoLogin/{kakaoAccessToken}")
	public String kakaoLoginRequest(@PathVariable("kakaoAccessToken") String kakaoAccessToken){
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<MultiValueMap<String, String>> kakaoRequest =
                new HttpEntity<>(headers);
        
        ResponseEntity<String> response = rt.exchange(
        	      "https://kapi.kakao.com/v1/user/access_token_info",
        	      HttpMethod.GET,
        	      kakaoRequest,
        	      String.class
        	);
//        final UserDetails userDetails = userService
//				.loadUserByUsername(response.);
//        final String token = jwtTokenUtil.generateToken(userDetails);
        return response.toString();//ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "네이버 로그인 검증")
	@GetMapping(value = "/naverLogin/{naverAccessToken}")
	public String naverLoginRequest(@PathVariable("naverAccessToken") String naverAccessToken){
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverAccessToken);
        
        HttpEntity<MultiValueMap<String, String>> naverRequest =
                new HttpEntity<>(headers);
        
        ResponseEntity<String> response = rt.exchange(
        		"https://openapi.naver.com/v1/nid/me",
        	      HttpMethod.GET,
        	      naverRequest,
        	      String.class
        	);
//        final UserDetails userDetails = userService
//				.loadUserByUsername(response.);
//        final String token = jwtTokenUtil.generateToken(userDetails);
        return response.toString();//ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "구글 로그인 검증")
	@GetMapping(value = "/googleLogin/{googleAccessToken}")
	public String googleLoginRequest(@PathVariable("googleAccessToken") String googleAccessToken){
		Payload payload = googleAccessToken.getPayload();
		GoogleIdToken idToken = verifier.verify(googleAccessToken);
		String userId = payload.getSubject();
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<MultiValueMap<String, String>> kakaoRequest =
                new HttpEntity<>(headers);
        
        ResponseEntity<String> response = rt.exchange(
        	      "https://kapi.kakao.com/v1/user/access_token_info",
        	      HttpMethod.GET,
        	      kakaoRequest,
        	      String.class
        	);
//        final UserDetails userDetails = userService
//				.loadUserByUsername(response.);
//        final String token = jwtTokenUtil.generateToken(userDetails);
        return response.toString();//ResponseEntity.ok(new JwtResponse(token));
	}
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public Response signup(@RequestBody @ApiParam(value = "가입 유저 정보") UserDto infoDto) {
        Response response = new Response();

        try {
            userService.save(infoDto);
            response.setResponse("success");
            response.setMessage("회원가입을 성공적으로 완료했습니다.");
        } catch (Exception e) {
            response.setResponse("failed");
            response.setMessage("회원가입을 하는 도중 오류가 발생했습니다.");
            response.setData(e.toString());
        }
        return response;
    }
    @ApiOperation(value = "이메일 중복확인")
    @GetMapping("/email/is-exist")
    public Boolean isExistEmail(@RequestParam("email") @ApiParam(value = "이메일") String email){
      return userService.isExistEmail(email);
    }
}