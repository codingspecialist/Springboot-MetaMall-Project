1. AOP 사용

 >BingResult를 사용하여 Valid 처리가 필요한 객체들에 대한 에러처리를 AOP 로 잡아서 만약 BindingResult에 hasErrors가 true 일 경우 응답을 AOP에서 대신한다.
이 기능은 애플리케이션이 실행하는동안 런타임에 동적으로 프록시 객체가 생성되며 해당 프록시 객체가 PointCut으로 지정한 @BindingCheck 애노테이션이 붙은 메소드를 호출할 경우 해당 호출을 가로채어 어드바이스를 실행한다.
런타임 시점에 애노테이션이 붙은 클래스의 프록시객체를 만들어 해당 프록시 객체를 통해 부가로직과 핵심로직이 함께 실행되는 것이다.
 > 
> 
> 이런 방식을 권한체크에서도 사용했는데 권한체크에서는 request



2. 내장객체
> Spring에서는 pageContext,request,session,application과 같은 JSP 내장 객체를 직접적으로 사용하는 것이 아닌, 서블릿 기반의 웹 애플리케이션에서는 HttpServletRequest, HttpSessopm, ServletContext와 같은 웹 관련 객체를 추상화하여 컨테이너에서 관리되는 빈을 사용한다.
> 따라서 Spring에서는 HttpServletRequest, HttpSession, ServletContest와 같은 웹 관련 객체를 사용하여 웹 애플리케이션의 상태를 관리하고 데이터를 공유할 수 있다.
> 일단 ServletContext는 모든 애플리케이션 내의 모든 서블릿들이 동일한 ServletContxt객체를 공유하기 때문에 접근 권한을 체크할 때 좋은 선택은 아니기에 두가지 선택권을 두고 판단했다. 바로 HttpServletRequest,, HttpServletResponse이다. 


>- HttpServletRequest  - HTTP 요청 단위의 데이터를 저장하는 객체이다. 요청이 들어올 때 생성되고, 응답이 완료되면 소멸된다. 따라서 HttpServletRequest를 통해 저장된 데이터는 해당 요청의 처리가 완료되면 사라진다. HttpServletRequest는 주로 사용자의 요청에 대한 처리를 위해 사용되며 요청 단위의 데이터를 임시적으로 저장하고 싶을 때 유용하다.
>- HttpSession - HttpSession은 웹 애플리케이션 단위의 데이터를 저장하는 객체이다. HttpSession은 웹 브라우저와 웹 서버 간의 연결을 유지하며, 서버 측에서 웹 브라우저의 상태를 유지할 수 있도록 해준다. HttpSession은 웹 브라우저를 종료하거나 세션의 유효시간이 만료될 때까지 데이터를 보존하며 다양한 클라이언트 요청 간에 공유할 수 있다.

3. 내 생각
> 해당 위에 설명대로 권한을 만약 HttpSession에 저장하게 된다면 사용자의 정보를 저장하게 된다. 그런데 JWT토큰을 사용하는데 굳이 사용자의 정보를 서버에 저장을 해야하나 싶은 의문이 들어서 나는 해당 애플리케이션에서는 Request를 사용했다.
> "세션을 사용하는게 더 좋을 수 있지 않을까?" 생각을 잠시했지만 요청이 들어오면 해당 세션에 데이터가 있는지 조회하고 없으면 저장하고 이러한 로직 자체가 request를 사용하여 바로 해당 요청이 들어오면저장하고 요청이 끝나면 삭제되는 방식이 더 효율적이고 JWT 토큰을 사용하는 방식에 맞는 방식이라는 생각이 들었다.
> 
> 기존 Filter를 삭제하고 OncePerRequestFilter 를 상속받는 filter를 구현하는 방식으로 변경했다. 나중에 시큐리티를 사용한다면 필터를 시큐리티 설정 클래스에 등록하여 사용해야 하는데 등록하려면 설정클래스에 등록한 방식과 겹치면서 빈으로 두번 등록해버리는 사고가 있을 수도 있다는 생각이 든다. 그래서 기존에 사용하던 방식말고 해당 Filter를 상속하는 클래스를 빈으로 등록하는 방식으로 변경했다.
> 근데 이런 사고가 없을 수 있다고 생각하지만 구글에 쳐봐도 많기에 Filter는 사용할 경우 로그를 사용해서 두번 실행되지는 않는지 체크가 꼭 필요하다.
> OncePerRequestFilter를 상속하는 클래스를 빈으로 두 번 등록해도 한 번 실행하는 걸 보장한다.
> 
```
Spring guarantees that the OncePerRequestFilter is executed only once for a given request.

- https://www.baeldung.com/spring-onceperrequestfilter

```




/order/save POST 권한  (인증필요, CUSTOMER 권한 필요)

```json
[
  {
    "product": {
      "id": 50,
      "name": "TEst50",
      "price": 350,
      "qty": 50,
      "createdAt": "2023-04-07T22:40:53.544879",
      "updatedAt": null
    },
    "count": 4,
    "orderPrice": 4000
  }
]

```

/order/save RESPONSE

```json
{
    "status": 200,
    "msg": "success",
    "data": {
        "id": 2,
        "user": null,
        "orderProductList": [
            {
                "id": 2,
                "product": {
                    "id": 50,
                    "name": "TEst50",
                    "price": 350,
                    "qty": 50,
                    "createdAt": "2023-04-07T22:40:53.544879",
                    "updatedAt": null
                },
                "count": 4,
                "orderPrice": 4000,
                "createdAt": "2023-04-07T22:47:27.6473413",
                "updatedAt": null,
                "productName": null,
                "orderSheet": null
            }
        ],
        "totalPrice": 4000,
        "createdAt": "2023-04-07T22:47:27.6448262",
        "updatedAt": null,
        "username": "customer"
    }
}

```