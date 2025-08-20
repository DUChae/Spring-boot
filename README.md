# 📖 Spring Boot 기반 게시판 웹 애플리케이션

<br>

### 📌 **프로젝트 개요**
Spring Boot와 JPA를 활용하여 게시글, 댓글, 회원 기능을 갖춘 풀스택 웹 애플리케이션의 백엔드를 구현했습니다. 전통적인 **MVC 방식**과 현대적인 **REST API 방식**을 모두 적용하며 백엔드 개발의 핵심 원리를 학습하고 실무 적용 능력을 길렀습니다.


* **역할**: 백엔드 개발 (개인 프로젝트)

---

### 🤔 **프로젝트를 시작한 이유**
웹 백엔드 개발의 전반적인 흐름과 핵심 개념을 깊이 이해하고자 게시판 구현을 선택했습니다. 특히, 단순히 기능을 만드는 것을 넘어 **MVC와 RESTful API 아키텍처의 차이점과 공존 방식**, 그리고 **Spring Security를 활용한 견고한 인증/인가 시스템 구축**에 대한 실질적인 경험을 쌓는 것을 목표로 삼았습니다. 이는 향후 대규모 서비스 개발 역량의 초석을 다지는 데 필수적이라고 판단했습니다.

---

### 🛠️ **기술 스택**
* **Backend**: `Java 17`, `Spring Boot 3.x`, `Spring Security`, `Spring Data JPA`, `Lombok`
* **Database**: `H2 Database` (개발용 인메모리 DB)
* **환경/툴**: `IntelliJ IDEA`, `Postman` (API 테스트)

---

### ✨ **핵심 기능**

* **회원 기능**
    * `Spring Security`를 활용한 안전한 회원가입 (`BCrypt` 비밀번호 암호화), 로그인, 로그아웃 처리
    * 사용자 `권한(Role)` 기반의 접근 제어(`@PreAuthorize`) 구현 예정
* **게시글 기능 (RESTful API 포함)**
    * 게시글 목록 조회: `GET /api/posts` (전체 게시글 JSON 반환)
    * 게시글 상세 조회: `GET /api/posts/{postId}` (특정 게시글 JSON 반환)
    * 게시글 등록: `POST /api/posts` (JSON 형식으로 게시글 데이터 수신 및 생성)
    * 게시글 수정: `PUT /api/posts/{postId}` (게시글 업데이트)
    * 게시글 삭제: `DELETE /api/posts/{postId}`
* **댓글 기능 (RESTful API 포함)**
    * 특정 게시글에 종속된 댓글 목록 조회: `GET /api/posts/{postId}/comments`
    * 댓글 등록: `POST /api/posts/{postId}/comments` (작성자 검증 포함)
    * 댓글 수정: `PUT /api/posts/{postId}/comments/{commentId}` (댓글 내용 수정, 작성자 검증 포함)
    * 댓글 삭제: `DELETE /api/posts/{postId}/comments/{commentId}` (작성자 검증 및 게시글-댓글 관계 무결성 유지)
* **좋아요 기능 (RESTful API 포함)**
    * 게시글 좋아요 토글: `POST /api/posts/{postId}/likes` (좋아요 추가/취소 처리)
    * 사용자의 중복 좋아요 요청 방지 로직 적용
    * 게시글별 총 좋아요 수 카운트 기능

---

### 💡 **기술적 도전 & 해결**

#### 1. **MVC와 REST API 요청의 `SecurityFilterChain` 충돌 문제 해결**
* **상황**: Spring Security 기본 설정상 웹 페이지 요청 시 로그인 페이지로 `302 Redirect`가 발생하는데, 이 필터 체인이 Postman을 통한 REST API 요청에도 적용되어 API 호출 시에도 `302` 오류가 발생했습니다.
* **해결**: `WebSecurityConfigurerAdapter`를 확장하여 두 개의 `SecurityFilterChain` Bean을 명시적으로 선언했습니다. `/api/**` 경로에 대해서는 `requestMatchers()`로 정의하고, `authorizeHttpRequests().anyRequest().permitAll()`을 적용하여 로그인 리다이렉트 없이 API 접근을 허용했습니다. 이와 함께 `formLogin().disable()` 및 `csrf().disable()` 설정도 추가했습니다.

#### 2. **REST API 테스트 환경에서의 `@AuthenticationPrincipal` `null` 문제**
* **상황**: Spring Security가 `permitAll()`로 설정된 API 경로에서는 `@AuthenticationPrincipal UserDetails userDetails` 파라미터에 인증 정보가 주입되지 않아 `NullPointerException`이 발생했습니다. 이는 실제 사용자 로그인 없이 API 기능을 검증하는 데 방해가 되었습니다.
* **해결**: 개발 및 테스트 목적으로 `userDetails.getUsername()`을 직접 호출하는 대신, `"test"`라는 하드코딩된 사용자 이름을 사용하도록 임시 코드를 삽입했습니다. 이 방법으로 API의 비즈니스 로직 및 응답 검증에 집중할 수 있었으며, 실제 배포 단계에서는 `JWT(JSON Web Token)` 기반의 안전한 토큰 인증 시스템으로 전환하여 이 문제를 근본적으로 해결할 계획입니다.

---

### 🎯 **성과 및 배운 점**
* **실전 아키텍처 이해**: 전통적인 **MVC 방식**의 한계를 직접 경험하며, **REST API**를 통한 **백엔드-프론트엔드 분리(Decoupled Architecture)**의 필요성과 `API-First` 개발 방식의 장점을 깊이 이해하고 실천했습니다.
* **Spring Security 심화**: `SecurityFilterChain` 분리, 인증/인가 흐름, 세션 vs 토큰 기반 인증의 차이점을 실무적으로 체험하며 `Spring Security` 설정 및 활용 능력을 크게 향상시켰습니다.
* **문제 해결 능력**: `302 리다이렉트` 및 `NullPointerException`과 같은 복잡한 기술적 문제들을 직접 분석하고 해결하는 과정을 통해 **디버깅 능력**과 **주도적인 문제 해결 능력**을 길렀습니다.

---

### 🔜 **다음 단계 (Future Plans)**
이 프로젝트는 웹 백엔드의 핵심 개념을 다지는 데 큰 도움이 되었습니다. 다음 목표는 순수 **REST API**와 `React/Vue.js`와 같은 프론트엔드 프레임워크를 연동하여 **풀스택 애플리케이션 개발 경험**을 쌓는 것입니다. 이를 통해 실무에 즉시 투입될 수 있는 개발자로 성장할 것입니다.
