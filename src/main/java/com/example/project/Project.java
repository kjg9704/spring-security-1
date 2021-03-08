package com.example.project;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;

@Entity
@Table(name="project_post")
@Getter
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	@NotBlank
	private String title;
	
//	@NotBlank
//	private String contact;

//	나중에 USER API나오면 연동
//	@ManyToOne
//	@JoinColumn
//	private User author;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
//	private int recruiting;
//	private int applicant;
	
	@Enumerated(EnumType.STRING)
	private ERegion region;
//	
//	private int duration;
//	
	private String shortDesc;
	
////	topic과 차별성 고려
//	private String purpose;
//	
////	필요할지 생각해보기
//	private boolean privacy;
//	
//	private int clicks;
//	
////	목적이랑 차별성 생각해보기
//	private String topic; 
//	
//	private int recruitmentPeriod;
//	
//	projectStack에 넣음
//	private ProjectStack stack;
	@Enumerated(EnumType.STRING)
	@OneToMany(fetch = FetchType.LAZY, mappedBy="stack")
	private final List<ProjectStack> stacks = new ArrayList<>();
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="")
//	private final List<ProjectWrite> write = new ArrayList<>();
//	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="")
////	private final List<ProjectKeep> keeps = new ArrayList<>();
////	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="")
//	private final List<ProjectComment> comments = new ArrayList<>();
//	
//	
//	public static Project of(String shortdesc, User author) {
//		Project project = new Project();
//		project.shortDesc = shortdesc;
////		project.author = author;
//		return project;
//	}
//	test code -> without user
	public static Project of(String shortdesc, String title) {
		Project project = new Project();
		project.title = title;
		project.shortDesc = shortdesc;
		return project;
	}
	public void update(String title, String shortDesc) {
		this.shortDesc = shortDesc;
	}
}
