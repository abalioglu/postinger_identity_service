package com.kafein.intern.postinger_identity_service.model;

import lombok.Data;

@Data
public class Post {

    private String id;
    private String imageUrl;
    private String description;
    private Long userId;
}
