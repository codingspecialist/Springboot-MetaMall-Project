# 상품주문서비스 토이프로젝트
___
## DATE
2023.04.05 ~ 2023.04.09
___
## STACKS

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)

### DEPENDENCIES
```
implementation group: 'com.auth0', name: 'java-jwt', version: '4.3.0'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-web'
compileOnly 'org.projectlombok:lombok'
developmentOnly 'org.springframework.boot:spring-boot-devtools'
runtimeOnly 'com.h2database:h2'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```
___
## 기능
___
### 회원가입
### 로그인
- jwt 응답
### 상품등록
- 인증필요, 판매자권한
### 상풍목록보기
- 인증필요, 권한없음
### 상품상세보기
- 인증필요, 권한없음
### 상품수정하기
- 인증필요, 판매자권한
### 주문하기
- 인증필요, 고객권한
### 주문목록보기
- 인증필요, 고객권한(본인주문), 판매자권한(본인주문), 관리자권한(모든주문)
### 주문취소하기
- CasCase 활용
- 인증필요, 고객권한, 판매자권한
___
## ERD
[ERD](https://dbdiagram.io/d/642e523a5758ac5f172722f4)
___
## TREE
___
## API
| Page | Action | Method | URL            | Request                                                                                                                                              | Response                       | 비고 |
|------|--------| ------ |----------------|------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------| --- |
| 회원가입 | 회원가입   | POST | /api/user/join | {<br/>"username":"username",<br/>"password":"password",<br/><br/>"password_check":"passsword",<br/>"email":"email@email.com",<br/>"role":"USER"<br/>} | {"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} | |
| 로그인  | 로그인 | POST | /api/user/login | {<br/>"username":"username",<br/>"password":"password"<br/>}                                                                                         | {"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/> ||           

