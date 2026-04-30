# BackOffice Project

> 이커머스 서비스 운영을 위한 **백오피스 관리자 시스템**입니다.  
> 관리자 계정, 고객, 상품, 주문 정보를 관리하고, 주문 생성/취소에 따른 재고 반영과 주문 상태 흐름 검증을 처리합니다.

<br>

## 1. 프로젝트 소개

**BackOffice Project**는 이커머스 서비스 운영자가 사용하는 백오피스 시스템입니다.

관리자는 이 시스템을 통해 다음 데이터를 관리할 수 있습니다.

- 관리자 계정
- 고객 정보
- 상품 정보
- 주문 정보

단순 CRUD 구현에 그치지 않고, 실제 운영 환경을 고려하여 다음 기능을 포함했습니다.

- 관리자 회원가입 / 로그인 / 로그아웃
- 관리자 목록 / 상세 조회 / 수정 / 승인 / 거부 / 삭제
- 고객 목록 / 상세 조회 / 수정 / 삭제
- 상품 등록 / 목록 조회 / 상세 조회 / 수정 / 재고 변경 / 상태 변경 / 삭제
- 주문 생성 / 목록 조회 / 상세 조회 / 상태 변경 / 취소
- Cookie / Session 기반 인증
- 주문 생성 및 취소 시 상품 재고 반영
- 주문 상태 흐름 검증 및 예외 처리

<br>

## 2. 프로젝트 목표

이 프로젝트의 목표는 다음과 같습니다.

- Spring Boot 기반 백엔드 애플리케이션 구조 이해
- 3-Layer Architecture 적용
- JPA 연관관계 및 엔티티 설계 경험
- Cookie / Session 기반 인증 구현
- 도메인 간 데이터 흐름과 비즈니스 로직 처리 경험
- Git / GitHub 기반 협업 및 PR 리뷰 경험

<br>

## 3. 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| ORM | Spring Data JPA |
| Database | MySQL |
| Authentication | Cookie / Session |
| Collaboration | Git, GitHub |
| API Test | Postman |

<br>

## 4. 주요 기능

### 4.1 관리자 관리

- 관리자 회원가입
- 관리자 로그인 / 로그아웃
- 관리자 목록 조회
- 관리자 상세 조회
- 관리자 정보 수정
- 관리자 승인 / 거부
- 관리자 삭제

### 4.2 고객 관리

- 고객 목록 조회
- 고객 상세 조회
- 고객 정보 수정
- 고객 삭제

### 4.3 상품 관리

- 상품 등록
- 상품 목록 조회
- 상품 상세 조회
- 상품 정보 수정
- 상품 재고 변경
- 상품 상태 변경
- 상품 삭제

### 4.4 주문 관리

- 주문 생성
- 주문 목록 조회
- 주문 상세 조회
- 주문 상태 변경
- 주문 취소

<br>

## 5. 아키텍처

본 프로젝트는 **3-Layer Architecture**를 기반으로 구성했습니다.

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

| Layer | 역할 |
|---|---|
| Controller | HTTP 요청/응답 처리, Request DTO 검증 |
| Service | 비즈니스 로직 처리, 도메인 간 흐름 제어 |
| Repository | DB 접근, JPA 기반 데이터 조회/저장 |

<br>

## 6. 패키지 구조

```text
src/main/java/com/sparta/cch/backofficeproject
├── admin
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── init
│   ├── repository
│   └── service
├── customer
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── init
│   ├── repository
│   └── service
├── product
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── repository
│   └── service
├── order
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
└── common
    ├── config
    ├── dto
    ├── entity
    ├── exception
    └── session
```

<br>

## 7. 주문 도메인 핵심 비즈니스 로직

### 7.1 주문 상태 변경 규칙

주문 상태는 다음 흐름만 허용합니다.

```text
PENDING → SHIPPING → COMPLETED
```

| 현재 상태 | 변경 가능 상태 | 설명 |
|---|---|---|
| PENDING | SHIPPING | 준비중 주문은 배송중으로 변경 가능 |
| SHIPPING | COMPLETED | 배송중 주문은 배송완료로 변경 가능 |
| COMPLETED | 변경 불가 | 완료된 주문은 상태 변경 불가 |
| CANCELED | 변경 불가 | 취소된 주문은 상태 변경 불가 |

<br>

### 7.2 주문 취소 규칙

- `PENDING` 상태인 주문만 취소할 수 있습니다.
- 취소 사유는 필수 입력값입니다.
- 주문 취소 시 주문 상태를 `CANCELED`로 변경합니다.
- 주문 취소 시 주문 수량만큼 상품 재고를 복구합니다.
- 상품 상태가 `DISCONTINUED`인 경우 재고만 복구하고 상태는 유지합니다.
- 그 외 상품은 복구된 재고 기준으로 상태를 자동 갱신합니다.

<br>

### 7.3 주문 생성 흐름

```text
관리자 세션 확인
  ↓
고객 존재 여부 확인
  ↓
상품 존재 여부 확인
  ↓
상품 상태 및 재고 검증
  ↓
주문 생성
  ↓
주문 수량만큼 상품 재고 차감
```

<br>

### 7.4 주문 취소 흐름

```text
관리자 세션 확인
  ↓
주문 존재 여부 확인
  ↓
주문 상태 검증
  ↓
취소 사유 검증
  ↓
주문 상태 CANCELED 변경
  ↓
상품 재고 복구
```

<br>

## 8. 데이터 처리 방식

### 8.1 상품 재고 / 상태 처리

상품은 재고 변경 시 다음 규칙을 따릅니다.

| 조건 | 상품 상태 |
|---|---|
| 재고 0 | SOLD_OUT |
| 재고 1 이상 | SALE |
| DISCONTINUED 상태 | 재고가 변해도 DISCONTINUED 유지 |

<br>

### 8.2 주문 생성 시 처리

- 상품 존재 여부 확인
- 고객 존재 여부 확인
- 관리자 세션 확인
- 상품 상태 검증
- 상품 재고 검증
- 주문 생성
- 상품 재고 차감

<br>

### 8.3 주문 취소 시 처리

- 주문 존재 여부 확인
- 주문 상태 검증
- 취소 사유 검증
- 주문 상태 변경
- 상품 재고 복구
- 상품 상태 자동 갱신

<br>

## 9. 인증 방식

본 프로젝트는 **Cookie / Session 기반 인증**을 적용했습니다.

- 로그인 성공 시 세션에 관리자 ID 저장
- 인증이 필요한 API는 세션에서 관리자 ID를 조회
- 로그인하지 않은 사용자는 인증이 필요한 API 접근 불가
- 로그아웃 시 세션 무효화

<br>

## 10. API 테스트

API 테스트는 **Postman**을 통해 진행했습니다.

### 주요 테스트 항목

- 관리자 회원가입 / 로그인 / 로그아웃
- 상품 등록 / 수정 / 재고 변경
- 주문 생성
- 주문 목록 조회
- 주문 상세 조회
- 주문 상태 변경
- 주문 취소

### 주문 도메인 예외 테스트

- 잘못된 주문 상태값
- 허용되지 않은 주문 상태 전이
- 잘못된 주문 ID
- 존재하지 않는 주문
- 취소 사유 누락
- 품절 상품 주문
- 단종 상품 주문
- 재고 부족 주문

<br>

## 11. 브랜치 전략

| 브랜치 | 역할 |
|---|---|
| `main` | 최종 배포 / 제출 브랜치 |
| `develop` | 통합 개발 브랜치 |
| `feat/도메인명` | 기능 개발 브랜치 |

### 예시

```text
feat/order
feat/product
feat/admin
```

<br>

## 12. 커밋 컨벤션

| 타입 | 설명 |
|---|---|
| `feat` | 기능 추가 |
| `fix` | 버그 수정 |
| `chore` | 기타 작업 |
| `refactor` | 리팩토링 |
| `test` | 테스트 코드 |
| `docs` | 문서 작업 |

### 예시

```text
feat: 주문 취소 API 추가
fix: 주문 상태 전이 검증 오류 수정
refactor: 주문 응답 DTO 변환 방식 개선
docs: README 프로젝트 소개 작성
```

<br>

## 13. 실행 방법

### 13.1 프로젝트 클론

```bash
git clone [repository-url]
```

### 13.2 프로젝트 폴더 이동

```bash
cd spring-backoffice
```

### 13.3 데이터베이스 생성

```sql
CREATE DATABASE backOffice_db;
```

### 13.4 DB 설정

`src/main/resources/application.properties` 파일에서 로컬 환경에 맞게 DB 정보를 설정합니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/backOffice_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

> `application.properties`에는 개인 DB 계정 정보가 포함될 수 있으므로 Git에 올리지 않도록 주의합니다.

### 13.5 애플리케이션 실행

IntelliJ에서 `BackOfficeProjectApplication`을 실행하거나, 아래 명령어로 실행합니다.

```bash
./gradlew bootRun
```
## 14. 기대 효과

이 프로젝트를 통해 다음과 같은 역량을 기를 수 있었습니다.

- 백오피스 시스템의 핵심 도메인 설계 경험
- JPA 기반 엔티티 관계 설계 경험
- 세션 기반 인증 처리 경험
- 비즈니스 로직 중심의 서비스 계층 설계 경험
- 팀 프로젝트 협업 및 PR 리뷰 경험
- 문제 발생 시 로그를 기반으로 원인을 분석하는 경험

<br>

## 15. 회고

이번 프로젝트를 통해 단순 CRUD 구현뿐 아니라, 도메인 간 연관관계와 비즈니스 규칙을 실제 코드로 녹여내는 경험을 할 수 있었습니다.

이를 통해 백엔드 개발에서 중요한 **도메인 로직의 책임 분리**와 **데이터 흐름 설계**의 중요성을 배울 수 있었습니다.

<br>

## 17. 팀원

| 이름 | 담당 도메인     |
|---|------------|
| 성채원 | 상품 관리      |
| 전은기 | 관리자, 고객 관리 |
| 임선구 | 주문 관리      |