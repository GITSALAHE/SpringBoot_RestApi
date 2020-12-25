package com.gitsalahapi.app.gitsalahapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gitsalahapi.app.gitsalahapi.model.Posts;

public interface Repo extends JpaRepository<Posts, Long> {
	List<Posts> findByPublished(boolean published);
	List<Posts> findByTitleContaining(String title);
}
