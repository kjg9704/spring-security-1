package com.example.project;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(description = "프로젝트 게시글 컨트롤러")
@RestController
@RequiredArgsConstructor
public class ProjectController {

	private ProjectService projectService;
	@ApiOperation(value = "로그인")
	@PostMapping("/project/scrap/{projectindex}")
	public ResponseEntity<?> addScrap(@PathVariable("projectindex") int projectIndex, @AuthenticationPrincipal User user) {
		projectService.addScrap(projectIndex, user.getUserIndex());
		return ResponseEntity.ok("success");
	}
}
