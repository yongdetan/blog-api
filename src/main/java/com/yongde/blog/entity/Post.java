package com.yongde.blog.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    // Using Long for id to prevent ID exhaustion and allow id to be null.
    // Long is a wrapper unlike long.
    // Did not specify the strategy for GeneratedValue, letting JPA pick the best based on chosen db
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private String category;

    // Basically creating a mapping table in the database to store the collections data structure.
    // ElementCollection is required, the remaining 2 is optional but is recommended if we want to be more specific.
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    // audit fields
    private Instant created;

    private Instant updated;

    public Post() {}

    // DO NOT include id to prevent caller from inserting id.
    // DO NOT include audit fields
    public Post(String title, String content, String category, List<String> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
    }

    private Post(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.category = builder.category;
        this.tags = builder.tags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String content;
        private String category;
        private List<String> tags;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

    @PrePersist
    public void onCreate() {
        this.created = Instant.now();
        this.updated = Instant.now();
    }
    @PreUpdate
    public void onUpdate() {
        this.updated = Instant.now();
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}
