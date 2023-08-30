package com.kafein.intern.postinger_identity_service.service;

import com.kafein.intern.postinger_identity_service.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "postinger-post-service", url = "http://localhost:8080")
public interface PostServiceClient {
    @GetMapping(value = "/posts/user-posts")
    public List<Post> getPosts(@RequestParam Long userId);
}
