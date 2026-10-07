# ⚡ ZIPLINK.IO — Backend

**High-Performance Spring Boot URL Shortener Microservice**

A robust, enterprise-grade Spring Boot microservice powering **ZIPLINK.IO**. Built with **Base62 encoding**, **Redis caching**, **Rate Limiting**, **PostgreSQL (Supabase)**, and **Docker**.

[Frontend Live Demo](https://ziplink-indol.vercel.app) • [Frontend Repository](https://github.com/InzmamKhan/Ziplink_Frontend)

---

## 🎨 Key Features

* **Sub-50ms Redirection Speed:** Leverages **Redis caching** to achieve ultra-fast `HTTP 302 Found` redirects.
* **Base62 Hash Generation:** Encodes database auto-incrementing primary keys or custom hashes into compact, web-safe short links (e.g., `ziplink.io/aB3x9`).
* **Custom Link Aliases:** Allows users to define custom human-readable URL slugs.
* **Rate Limiting & Anti-Abuse:** Integrated request throttling (e.g., Bucket4j / Spring Rate Limiter) to prevent API spam and DDoS attempts.
* **Database Persistency:** Asynchronous writes and relational mapping powered by **Spring Data JPA & PostgreSQL** on Supabase.
* **Click Analytics & Tracking:** Tracks total clicks and timestamp metrics for generated links.
* **Containerized Deployment:** Packaged as a multi-stage **Docker image** hosted on **Render**.

---

## 🛠️ Tech Stack

* **Framework:** Spring Boot 3.x (Java 17 / 21)
* **Build System:** Maven
* **Database:** PostgreSQL (Hosted on Supabase)
* **Caching Layer:** Redis (Upstash / Redis Labs)
* **ORM & Data Access:** Spring Data JPA / Hibernate
* **API Documentation:** Swagger UI & OpenAPI 3.0 (`/swagger-ui.html`)
* **Deployment & Containerization:** Docker, Render PaaS

---

## 📁 Folder Structure

```
Ziplink_Backend/
├── src/
│   ├── main/
│   │   ├── java/com/ziplink/
│   │   │   ├── config/          # CORS, Redis, Security, and OpenAPI Configurations
│   │   │   ├── controller/      # REST API Endpoints (UrlController, RedirectController)
│   │   │   ├── dto/             # Request & Response Data Transfer Objects
│   │   │   ├── exception/       # Global Exception Handler & Custom Errors
│   │   │   ├── model/           # JPA Entities (UrlMapping, Analytics)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── service/         # Core Business Logic & Base62 Encoding
│   │   │   └── util/            # Base62 Helpers, URL Cleaners
│   │   └── resources/
│   │       ├── application.yml  # Base Application Configuration
│   │       └── application-dev.yml
├── .env.example                 # Environment variables template
├── Dockerfile                   # Multi-stage Docker build config
├── pom.xml                      # Maven dependencies & build settings
└── README.md                    # Backend documentation
```

---

## 🚀 Getting Started

Follow these instructions to set up and run the Spring Boot service locally.

### Prerequisites

* **JDK 17** or **JDK 21** installed (`java -version`)
* **Maven 3.8+** (`mvn -v`)
* **Docker Desktop** (Optional, for local Redis/Postgres container setups)

---

### Local Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/InzmamKhan/Ziplink_Backend.git
   cd Ziplink_Backend
   ```

2. **Configure Environment Variables:**
   Duplicate the `.env.example` file or set the following environment variables in your IDE (IntelliJ/Eclipse) or terminal:

   ```bash
   export DB_URL=jdbc:postgresql://<SUPABASE_HOST>:5432/postgres
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_db_password
   export REDIS_HOST=localhost
   export REDIS_PORT=6379
   export REDIS_PASSWORD=your_redis_password
   export APP_BASE_URL=http://localhost:8080
   ```

3. **Build the application:**
   ```bash
   mvn clean compile
   ```

4. **Run the local development server:**
   ```bash
   mvn spring-boot:run
   ```

The application will launch on **`http://localhost:8080`**.

---

## ⚙️ Environment Variables Reference

| Variable Name | Required | Description | Example / Default |
| :--- | :---: | :--- | :--- |
| `DB_URL` | **Yes** | JDBC connection URL for PostgreSQL | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` |
| `DB_USERNAME` | **Yes** | Database username | `postgres` |
| `DB_PASSWORD` | **Yes** | Database user password | `your_secure_password` |
| `REDIS_HOST` | **Yes** | Redis cache server host | `redis-12345.c1.us-east-1-2.ec2.cloud.redislabs.com` |
| `REDIS_PORT` | **Yes** | Redis cache server port | `6379` |
| `REDIS_PASSWORD` | Optional | Redis authentication password | `your_redis_auth_key` |
| `APP_BASE_URL` | **Yes** | Production/Local Short Link Domain | `https://ziplink-a3k1.onrender.com` |

---

## 🌐 API Reference

### 1. Shorten a Long URL
* **Endpoint:** `POST /api/v1/urls/shorten`
* **Request Body:**
  ```json
  {
    "originalUrl": "https://wikipedia.org/wiki/List_of_lists_of_lists",
    "customAlias": "wiki-lists"
  }
  ```
* **Response:** `201 Created`
  ```json
  {
    "shortUrl": "https://ziplink-a3k1.onrender.com/wiki-lists",
    "originalUrl": "https://wikipedia.org/wiki/List_of_lists_of_lists",
    "alias": "wiki-lists",
    "createdAt": "2026-10-07T23:00:00Z"
  }
  ```

---

### 2. Redirect Short URL
* **Endpoint:** `GET /{shortKey}`
* **Response:** `302 Found` (Redirects directly to `originalUrl`)

---

### 3. Get Link Analytics
* **Endpoint:** `GET /api/v1/urls/{shortKey}/analytics`
* **Response:** `200 OK`
  ```json
  {
    "shortKey": "wiki-lists",
    "clickCount": 128,
    "lastAccessedAt": "2026-10-07T23:45:00Z"
  }
  ```

---

## ⚡ Caching & Redirection Architecture Flow

```
sequenceDiagram
    autonumber
    actor User
    participant Spring as Spring Boot Controller
    participant Redis as Redis Cache
    participant Postgres as Supabase Database

    User->>Spring: GET /{shortKey}
    Spring->>Redis: GET url:{shortKey}
    
    alt Cache Hit (Fast Path)
        Redis-->>Spring: Returns originalUrl
    else Cache Miss (Slow Path)
        Redis-->>Spring: Key not found
        Spring->>Postgres: SELECT original_url FROM urls WHERE key = shortKey
        Postgres-->>Spring: Returns URL Record
        Spring->>Redis: SET url:{shortKey} originalUrl (TTL: 24h)
    end

    Spring->>Postgres: Async Increment Click Count
    Spring-->>User: HTTP 302 Redirect to originalUrl
```

---

## 🐳 Docker Deployment

To build and test the production Docker container locally:

1. **Build the Docker Image:**
   ```bash
   docker build -t ziplink-backend .
   ```

2. **Run the Container:**
   ```bash
   docker run -p 8080:8080 --env-file .env ziplink-backend
   ```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for details.