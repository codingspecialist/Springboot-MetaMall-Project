# 상품주문서비스 토이프로젝트

## DATE
2023.04.05 ~ 2023.04.09

<br/>

## STACKS

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)

<br/>

### DEPENDENCIES
```
implementation group: 'com.auth0', name: 'java-jwt', version: '4.3.0'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-aop'
implementation 'org.springframework.boot:spring-boot-starter-validation'
compileOnly 'org.projectlombok:lombok'
developmentOnly 'org.springframework.boot:spring-boot-devtools'
runtimeOnly 'com.h2database:h2'
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

<br/>

## 기능

### 회원가입

<br/>

### 로그인
- jwt 응답

<br/>

### 상품등록
- 인증필요, 판매자권한

<br/>

### 상풍목록보기
- 인증필요, 권한없음

<br/>

### 상품상세보기
- 인증필요, 권한없음

<br/>

### 상품수정하기
- 인증필요, 판매자권한

<br/>

### 주문하기
- 인증필요, 고객권한

<br/>

### 주문목록보기
- 인증필요, 고객권한(본인주문), 판매자권한(본인주문), 관리자권한(모든주문)

<br/>

### 주문취소하기
- CasCase 활용
- 인증필요, 고객권한, 판매자권한

<br/>

## ERD
![ERD](https://user-images.githubusercontent.com/107831692/230870048-a42ba4a0-a6d3-43ce-ab69-bdbd64121380.png)

<br/>

## API
| Page | Action               | Method | URL              | Request                                                                                                                                       | Response                       |
|------|----------------------|--------|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| 회원가입 | signUp               | POST   | /api/user/signup | {<br/>"username":"username",<br/>"password":"password",<br/><br/>"password_check":"passsword",<br/>"email":"email@email.com",<br/>"role":"USER"<br/>} | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 로그인  | login                | POST   | /api/user/login  | {<br/>"username":"username",<br/>"password":"password"<br/>}                                                                                  | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/> |         
| 상품등록 | registerProduct      | POST   | /api/product     | {<br/>"name":"name",<br/>"price":1000,<br/>"qty":1<br/>}                                                                                      | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>}|
| 상품목록 | getProducts          | GET    |  /api/products   |                                                                                                                                               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 상품상세 | getProductDetails    | GET    | /api/product/{id} |                                                                                                                                               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 상품수정 | updateProduct        | PUT    | /api/product/{id} | {<br/>"name":"name",<br/>"price":1000,<br/>"qty":1<br/>}                                                                                      | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 주문   | order                | POST   | /api/order        | {<br/>"products":[<br/>{<br/>"count":1,<br/>"product_id":1<br/>},<br/>{<br/>"count":1,<br/>"product_id":2<br/>},...<br/>]<br/>}               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 주문목록 | getOrders           | GET    | /api/orders       |                                                                                                                                               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 주문목록(관리자) | getAllOrders       | GET    | /api/admin/orders |                                                                                                                                               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
| 주문취소 | cancelOrder          | DELETE | /api/order/{id} |                                                                                                                                               | {<br/>"status":200,<br/>"msg":"성공",<br/>"data":{...}<br/>} |
