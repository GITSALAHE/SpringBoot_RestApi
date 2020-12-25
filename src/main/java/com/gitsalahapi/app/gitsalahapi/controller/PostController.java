package com.gitsalahapi.app.gitsalahapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gitsalahapi.app.gitsalahapi.model.Posts;
import com.gitsalahapi.app.gitsalahapi.repository.Repo;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/gitsalahapi")
public class PostController {

	@Autowired
	Repo repos;

	@GetMapping("/get")
	public ResponseEntity<List<Posts>> getAllPosts(@RequestParam(required = false) String title) {
		try {
			List<Posts> posts = new ArrayList<Posts>();

			if (title == null)
			repos.findAll().forEach(posts::add);
			else
			repos.findByTitleContaining(title).forEach(posts::add);

			if (posts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Posts> getPostById(@PathVariable("id") long id) {
		Optional<Posts> postsData = repos.findById(id);

		if (postsData.isPresent()) {
			return new ResponseEntity<>(postsData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Posts> createPost(@RequestBody Posts posts) {
		try {
			Posts _posts = repos
					.save(new Posts(posts.getTitle(), posts.getDescription(), false));
			return new ResponseEntity<>(_posts, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Posts> updatePosts(@PathVariable("id") long id, @RequestBody Posts post) {
		Optional<Posts> postsData = repos.findById(id);

		if (postsData.isPresent()) {
			Posts _posts = postsData.get();
			_posts.setTitle(post.getTitle());
			_posts.setDescription(post.getDescription());
			_posts.setPublished(post.isPublished());
			return new ResponseEntity<>(repos.save(_posts), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") long id) {
		try {
			repos.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<HttpStatus> deleteAllPosts() {
		try {
			repos.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/gitsalah/published")
	public ResponseEntity<List<Posts>> findByPub() {
		try {
			List<Posts> posts = repos.findByPublished(true);

			if (posts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
