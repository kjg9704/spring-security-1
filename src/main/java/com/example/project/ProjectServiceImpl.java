package com.example.project;

public class ProjectServiceImpl implements ProjectService {

	private ScrapRepository scrapRepo;
	@Override
	public void addScrap(int projectId, long userIndex) {
		scrapRepo.save(Scrap.builder()
				.postIndex(projectId)
				.userIndex(userIndex)
				.build());
	}

}
