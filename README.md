# Springboot-MetaMall-Project


# 기능정리
* 회원가입 (POST)
* 로그인 (POST)
  * JWT 토큰 응답
* 상품등록 (POST)
  * 인증 필요, 판매자 권한
* 상품목록보기 (GET)
  * 인증 필요, 권한 없음
* 상품상세보기 (GET)
  * 인증 필요, 권한 없음
* 상품수정하기 (PUT)
  * 인증 필요, 판매자 권한
* 상품삭제하기 (DELETE)
  * 인증 필요, 판매자 권한
* 주문하기 (POST) - OrderProduct를 생성하여, OrderSheet에 추가하세요
  * 인증 필요, 고객 권한
* 고객입장 - 주문목록보기 (GET)
  * 인증 필요, 고객 권한, 본인의 주문목록만 볼 수 있어야 함
* 판매자입장 - 주문목록보기 (GET)
  * 인증 필요, 판매자 권한, 모든 주문목록을 볼 수 있음
* 고객입장 - 주문취소하기 (DELETE) - Casecade 옵션을 활용하세요. (양방향 매핑)
  * 인증 필요, 고객 권한, 본인의 주문만 취소할 수 있다.
* 판매자입장 - 주문취소하기 (DELETE) - Casecade 옵션을 활용하세요. (양방향 매핑) 
  * 인증 필요, 판매자 권한


# REST API

* 회원가입
POST http://localhost:8081/join
```
{
    "username": "ssar",
    "password": "1234",
    "email": "ssar@nate.com",
    "role": "ADMIN"
}
```

* 로그인
POST http://localhost:8081/login
```
{
    "username": "ssar",
    "password": "1234"
}
```
* 상품 등록
POST http://localhost:8081/save
```
{
    "name": "computer",
    "price": 30000,
    "quantity": 100,
    "role": "SELLER"
}
```
* 상품 전체 조회
GET http://localhost:8081/listAll

* 상품 개별 조회
GET http://localhost:8081/product/1

* 상품 개별 수정
PUT http://localhost:8081/product/1

* 상품 삭제
DELETE http://localhost:8081/product/1

# 느낀점
* 전체 적인 이해도가 부족하여 문제가 발생 시 해결이 잘 되지 않음
