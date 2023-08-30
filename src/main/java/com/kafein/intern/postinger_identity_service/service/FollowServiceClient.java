package com.kafein.intern.postinger_identity_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "postinger-follow-service", url = "http://localhost:8083")
public interface FollowServiceClient {
    @GetMapping("/follow/following-list")
    public List<Long> getFollowedIds(@RequestParam(name = "followerId")Long followerId);
    @GetMapping("/follow/follower-list")
    public List<Long> getFollowerIds(@RequestParam(name = "followedId") Long followedId);
    @GetMapping("/follow/follower-count")
    public Long getFollowerCount(@RequestParam(name = "followedId") Long followedId);
    @GetMapping("/follow/followed-count")
    public Long getFollowedCount(@RequestParam(name = "followerId") Long followerId);
}

