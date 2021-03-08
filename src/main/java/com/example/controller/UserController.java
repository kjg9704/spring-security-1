package com.example.controller;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.config.JwtTokenUtil;



import com.example.config.JwtRequest;
import com.example.config.JwtResponse;
import com.example.config.JwtUserDetailsService;
import com.example.config.Response;
import com.example.config.UserDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final UserDetails userDetails = userService.loadUserByEmailAndLogintype(authenticationRequest.getEmail(), authenticationRequest.getLogintype());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "카카오 로그인 검증")
	@GetMapping(value = "/kakaoLogin/{kakaoAccessToken}")
	public ResponseEntity<?> kakaoLoginRequest(@PathVariable("kakaoAccessToken") String kakaoAccessToken){
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
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response.getBody());
        String id = jsonObject.get("id").toString();
        UserDetails userDetails;
        String token = "";
        try {
        	userDetails = userService.loadUserByEmailAndLogintype(id, "kakao");
        	token = jwtTokenUtil.generateToken(userDetails);
        } catch(Exception e) {
        	return new ResponseEntity<>("Member registration required",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "네이버 로그인 검증")
	@GetMapping(value = "/naverLogin")
	public ResponseEntity<?> naverLoginRequest(String naverAccessToken){
        String header = "Bearer " + naverAccessToken; // Bearer 다음에 공백 추가
        System.out.println("------ 헤더확인");
        System.out.println(header);
        StringBuffer response = new StringBuffer();
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            System.out.println(responseCode + "-------");
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
        	return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
        JsonObject res = (JsonObject) jsonObject.get("response");
        String id = res.get("id").toString();
        UserDetails userDetails;
        String token = "";
        try {
        	userDetails = userService.loadUserByEmailAndLogintype(id, "naver");
        	token = jwtTokenUtil.generateToken(userDetails);
        } catch(Exception e) {
        	return new ResponseEntity<>("Member registration required",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@ApiOperation(value = "구글 로그인 검증")
	@GetMapping(value = "/googleLogin")
	public ResponseEntity<?> googleLoginRequest(String googleAccessToken){
		RestTemplate rt = new RestTemplate();
        System.out.println("Authorization"+ "Bearer " + googleAccessToken);
        String response = rt.getForEntity("https://oauth2.googleapis.com/tokeninfo?id_token=" + googleAccessToken, String.class).toString();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        String id = jsonObject.get("sub").toString();
        UserDetails userDetails;
        String token = "";
        try {
        	userDetails = userService.loadUserByEmailAndLogintype(id, "google");
        	token = jwtTokenUtil.generateToken(userDetails);
        } catch(Exception e) {
        	return new ResponseEntity<>("Member registration required",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new JwtResponse(token));
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
	@DeleteMapping("/withdraw")
	public ResponseEntity<?> withDraw(@AuthenticationPrincipal com.example.config.User user) {
		Response response = new Response();
		try {
			String userEmail = user.getUsername();
			String loginType = userService.getLoginType(user);
			userService.deleteUser(userEmail, loginType);
			userService.deletedSave(userEmail, loginType);
			response.setMessage("success");
		} catch(Exception e) {
			return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(response);
	}
	
    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @ApiParam(value = "가입 유저 정보") UserDto infoDto) {
        Response response = new Response();
        try {
            userService.save(infoDto);
            response.setMessage("success");
        } catch (Exception e) {
            response.setMessage("failed");
            return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
    @ApiOperation(value = "이메일 중복확인")
    @GetMapping("/email/is-exist")
    public Boolean isExistEmail(@RequestParam("email") @ApiParam(value = "이메일") String email){
      return userService.isExistEmail(email);
    }
}