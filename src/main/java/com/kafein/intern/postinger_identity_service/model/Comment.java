package com.kafein.intern.postinger_identity_service.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class Comment {
    private Long id;
    private Long userId;
    private String postId;
    private String content;
}
