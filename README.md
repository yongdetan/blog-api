# Blogging Platform

A RESTful blog API built with Spring Boot. Currently in active development.

## Description
A personal project to build a blogging platform from the ground up. Starting with a backend API, with plans to add authentication, authorization, and a modern frontend.
   
**What's Working:**
- ✅ Basic CRUD operations for Post
- ✅ Unit tests for PostController, PostRepository, PostService with JUnit and Mockito
- ✅ Exception handling for Post
- ✅ CI pipeline using GitHub Actions

## Tech Stack
**Backend:**
- Java 17+
- Spring Boot 4.0.2
- Maven

**Spring Boot Dependencies:**
- Spring Web
- Spring Data JPA

**Planned:**
- PostgreSQL
- Frontend framework (TBD - Probably React)

### Phase 1: Foundation & Authentication
- [x] Post entity with basic CRUD
- [ ] User entity with basic CRUD
- [ ] User authentication 
- [ ] Role-based authorization (User, Admin)

### Phase 2: Content Features & Persistence
- [ ] Migrate to PostgreSQL (H2 for testing, Postgres for main)
- [ ] Comment entity with basic CRUD
- [ ] Pagination
- [ ] Category & Tag entities (separate from Post)
- [ ] Admin-only APIs for managing categories/tags
- [ ] Post filtering and search

### Phase 3: Advanced Features
- [ ] Rate limiting
- [ ] API documentation (Swagger/OpenAPI)

### Phase 4: Frontend Development
- [ ] Choose frontend framework
- [ ] Public blog UI
- [ ] Admin dashboard

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

## API Endpoints (Current)

### Posts
- `POST /api/v1/posts` - Create new post
- `GET /api/v1/posts` - Get all posts
- `GET /api/v1/posts/{postId}` - Get post by ID
- `PUT /api/v1/posts/{postId}` - Update post
- `DELETE /api/v1/posts/{postId}` - Delete post
