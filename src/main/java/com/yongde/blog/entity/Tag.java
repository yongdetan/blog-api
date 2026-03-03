package com.yongde.blog.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    public Tag() {}

    public Tag(String name) {

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Tag name is required.");
        }
        this.name = name;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return id != null && id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
