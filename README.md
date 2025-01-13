# 143 초성게임

인지능력 향상 및 디지털 치매 예방을 위해 제작된 프로젝트

# Stack

* 사용언어 : Java 17
* 개발도구 : Spring Boot 3.2.3, Spring Data, JPA, QueryDsl
* 서버 : Apache Tomcat, JWT (JSON Web Token)
* DB : MariaDB (AWS RDS), Redis
* 배포 : AWS EC2
* 형상관리 :  Git

# 구현 기능
### 👬 회원 및 관리자
- 회원가입: 닉네임/이메일 중복 확인
- 로그인/로그아웃(JWT, Redis)
- 프로필 이미지 업로드(AWS S3)

### 🎮 게임
- 주제 조회: 뱃지 상태별 조회 (획득/미획득 구분)
- 게임 시작: 랜덤 주제 선택 및 문제 조회 (사용자가 푼 문제 제외)
- 문제 제시: 랜덤 문제 제공
- 게임 답변 저장: 사용자 답변 저장, 하트 수 및 뱃지 상태 업데이트
- 중도 이탈 처리: 통계 목적으로 데이터 저장

# 아키텍처 다이어그램

![image](https://github.com/mirikwon427/143quiz_backend/blob/main/src/main/java/garlicbears/quiz/image/Architecture%20Diagram.drawio.png)

# ERD

![image](https://github.com/mirikwon427/143quiz_backend/blob/main/src/main/java/garlicbears/quiz/image/143chosung-ERD.png)

# Getting Started

Click this [URL](https://garlicbears.github.io/143quiz_frontend/)!!


