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

### Phase 1: Core Blog Backend 
- [x] Post entity with CRUD operations
- [x] User entity with CRUD operations
- [x] User authentication with JWT bearer token
- [x] Role-based authorization (User, Admin)
- [x] PostgreSQL with Testcontainers for testing
- [x] Flyway database migrations
- [ ] Category & Tag entities with admin-only management *working on this now.

### Phase 2: Production-Ready Backend
- [ ] Pagination for all list endpoints
- [ ] Post filtering and search (by category, tag, author, date)
- [ ] Rate limiting
- [ ] API documentation (Swagger/OpenAPI) 

### Phase 3: Frontend Development
- [ ] Set up React framework (Vite/Next.js)
- [ ] Public blog interface
    - [ ] Homepage with post listings
    - [ ] Single post view
    - [ ] Category/tag filtering
    - [ ] Search functionality
- [ ] Admin dashboard
    - [ ] Post management (create, edit, delete, publish/draft)
    - [ ] Category/tag management
    - [ ] Basic analytics

### Phase 4: Engagement Features
- [ ] Comment system
- [ ] Like/reaction system
- [ ] Email subscription/newsletter
- [ ] User profiles
- [ ] Social sharing

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
