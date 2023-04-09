![thumbnail (3)](https://user-images.githubusercontent.com/92681117/230777373-47f9c91c-7bea-4a1b-adc7-27bed9b14843.png)

# 🛒쇼핑몰 프로젝트 
## 👩🏻‍💻참여
- 유현주

## ⚒️기술스택
- **SpringBoot**

## 🔧협업 도구 
- **Git**
- **GitHub**

## 🗂️데이터베이스
- **H2**

## ➕의존성 
```java
dependencies {
	implementation group: 'com.auth0', name: 'java-jwt', version: '4.3.0'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'junit:junit:4.13.1'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	}
```

## 🗒️구현 요청 사항
- 고객은 다수이다.
- 판매자는 한명이다.
- 고객은 여러건의 주문을 할 수 있다.
- 고객은 한번에 여러개의 상품을 주문할 수 있다.
- 고객은 상품 주문시에 개수를 선택할 수 있다.

## 💡구현된 기능 

### 🙋🏻‍♀️사용자 기능
  - 회원가입
  - 로그인

### 👜상품 관련 기능 
  - 판매자의 상품 등록
  - 등록된 상품들 목록 보기
  - 상품 상세보기
  - 판매자의 상품 수정
  - 판매자의 상품 삭제 

### 👩🏻‍💻주문 관련 기능 
  - 상품 주문 
  - 고객 주문 목록 보기 
  - 판매자의 전체 고객 주문 목록 보기
   
**상세한 요청과 응답은 [api문서](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/main/java/shop/mtcoding/metamall/api)를 확인** 

## 🔗ER-Diagram
<img width="856" alt="image" src="https://user-images.githubusercontent.com/92681117/230779121-38a29526-dc1f-4830-a1b2-1261939c7658.png">


## 👩🏻‍🔧작성한 테스트 코드
- 레포지토리 쿼리 테스트 코드

  [고객 쿼리](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/model/user/UserRepositoryTest.java)
  
  [제품 쿼리](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/model/product/ProductRepositoryTest.java)
  
  [주문서 쿼리](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/model/ordersheet/OrderSheetRepositoryTest.java)
  
- 컨트롤러 테스트 코드 

  [고객 컨트롤러](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/controller/UserControllerTest.java)
  
  [상품 컨트롤러](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/controller/ProductControllerTest.java)
  
  [주문 컨트롤러](https://github.com/yhj1129/Springboot-MetaMall-Project/blob/main/src/test/java/shop/mtcoding/metamall/controller/OrderControllerTest.java)
  
## 🔧보완할 점
- 요청에 대해 Dto로 받지 못한 것들이 몇 가지 있었다. 새로 Dto를 추가해서 요청에 맞는 Dto로 요청을 처리하도록 변경할 예정이다. 
- 유효성 검사 추가. 현재 null, "" 체크 정도만 구현되어 있다.
- 데이터베이스 연관관계를 좀 더 공부해서 응답이 더 깔끔하게 나올 수 있도록 변경할 예정. 불필요하게 많은 정보의 응답이 나온다고 느꼈다. 
- ER-Diagram을 보완하기
- AOP 적용 필요
- JWT를 헤더에서 가져와서 인증하는 부분이 계속해서 반복되는 것을 느껴 어노테이션으로 만들었지만 기존 메서드와 연결에서 어려움이 있어서 적용을 하지는 못했다.

## 🌟느낀 점
- 혼자 하는 프로젝트여서 협업에 큰 어려움이 없었지만, 다른 팀원들과 함께 하게 되면 버전 관리에 신경을 많이 써야할 것 같다. 
- 모르고 있던(배웠으나 까먹은..?) 부분도 스스로 검색하고 다시 공부하며 빈 부분이 많이 채워졌다고 느꼈다. 
- 지금까지 배운 내용이 다 들어있어서 뿌듯했다! 
