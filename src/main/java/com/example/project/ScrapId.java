package com.example.project;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Embeddable
public class ScrapId implements Serializable {

	 @OneToMany
	 @JoinColumn(name ="project_id")
	 private int postIndex;
	 
	 @OneToMany
	 @JoinColumn(name ="user_userIndex")
	 private long userIndex;
}
