package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.order.OrderSheetDto;
import shop.mtcoding.metamall.dto.product.OrderProductRequest;
import shop.mtcoding.metamall.model.log.error.ErrLogService;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderStatus;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class OrderController
{
    private final UserRepository userRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final ErrLogService err;

    @ResponseBody
    @Transactional
    @PostMapping("/order/create")
    public ResponseEntity<?> placeOrder(@RequestBody List<OrderProductRequest> orderProductRequests, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("USER")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(), "고객 권한이 필요합니다."), ""));
            //return ResponseEntity.status(403).body("고객 권한이 필요합니다.");
        }

        Optional<User> userOptional = userRepository.findById(loginUser.getId());

        if(userOptional.isPresent() == false)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(), "사용자 정보를 찾을 수 없습니다."), ""));
            //throw new RuntimeException("사용자 정보를 찾을 수 없습니다");
        }

        if(orderProductRequests.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(), "주문 내용이 없습니다."), ""));
        }

        User user = userOptional.get();

        OrderSheet orderSheet = OrderSheet.builder().user(user).orderStatus(OrderStatus.ORDER).build();

        int totalPrice = 0;
        for (OrderProductRequest orderProductRequest : orderProductRequests)
        {
            Optional<Product> OpProduct = productRepository.findById(orderProductRequest.getProductId());

            if(false == OpProduct.isPresent())
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(), "상품 정보를 찾을 수 없습니다."), ""));
            }

            Product product = OpProduct.get();

            int count = orderProductRequest.getCount();
            int orderPrice = product.getPrice() * count;

            try
            {
                product.removeStock(count);//영속성에 의한 처리됨
            }
            catch (Exception e)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(), "재고가 부족합니다"), ""));
            }

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .count(count)
                    .orderPrice(orderPrice)
                    .orderSheet(orderSheet)
                    .orderStatus(OrderStatus.ORDER)
                    .build();

            orderProductRepository.save(orderProduct);
            orderSheet.getOrderProductList().add(orderProduct);

            totalPrice += orderPrice;
        }
        orderSheet.setTotalPrice(totalPrice);

        OrderSheet savedOrderSheet = orderSheetRepository.save(orderSheet);

        ResponseDto<?> responseDto = new ResponseDto<>().data(savedOrderSheet);

        return ResponseEntity.ok(responseDto);
    }

    @ResponseBody
    @GetMapping("/orders/user")
    public ResponseEntity<?> getAllOrdersOfUser(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("USER")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"고객 권한이 필요합니다."), ""));

            //throw new Exception403("고객 권한이 필요합니다.");
        }

        List<OrderSheet> findSheets = orderSheetRepository.findByUserIdAndStatusNot(loginUser.getId(), OrderStatus.CANCEL);

        // OrderSheet에서 OrderSheetDto로 데이터를 옮기기
        List<OrderSheetDto> orderSheetDtos = findSheets.stream()
                .map(orderSheet -> new OrderSheetDto(orderSheet))
                .collect(Collectors.toList());

        ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetDtos );
        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @GetMapping("/orders/seller")
    public ResponseEntity<?> getAllOrdersOfSeller(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("SELLER")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"판매자 권한이 필요합니다."), ""));

            //throw new Exception403("판매자 권한이 필요합니다.");
        }

        List<OrderSheet> findSheets = orderSheetRepository.findByStatusNot(OrderStatus.CANCEL);

        // OrderSheet에서 OrderSheetDto로 데이터를 옮기기
        List<OrderSheetDto> orderSheetDtos = findSheets.stream()
                .map(orderSheet -> new OrderSheetDto(orderSheet))
                .collect(Collectors.toList());

        ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheetDtos);
        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @Transactional
    @DeleteMapping("/order/user/cancel/{orderId}")
    public ResponseEntity<?> cancelOrderOfUser(@PathVariable("orderId") Long orderId, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("USER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"고객 권한이 필요합니다."), ""));
            //throw new Exception403("고객 권한이 필요합니다.");
        }

        Optional<OrderSheet> orderSheetOptional = orderSheetRepository.findById(orderId);

        if (!orderSheetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"주문 정보를 찾을 수 없습니다."), ""));
            //throw new Exception404("주문 정보를 찾을 수 없습니다.");
        }

        OrderSheet orderSheet = orderSheetOptional.get();
        orderSheet.setStatus(OrderStatus.CANCEL);

        if(loginUser.getId() != orderSheet.getUser().getId())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"유저와 주문자가 불일치 합니다."), ""));
            //throw new Exception404("유저와 주문자가 불일치 합니다.");
        }


        for (OrderProduct orderProduct : orderSheet.getOrderProductList()) {
            orderProduct.setStatus(OrderStatus.CANCEL);
            Product product = orderProduct.getProduct();
            product.addStock(orderProduct.getCount());
            productRepository.save(product);
        }

        //OrderSheet updatedOrderSheet = orderSheetRepository.save(orderSheet);

        ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheet);
        return ResponseEntity.ok(responseDto);
    }

    @ResponseBody
    @Transactional
    @DeleteMapping("/order/seller/cancel/{orderId}")
    public ResponseEntity<?> cancelOrderOfSeller(@PathVariable("orderId") Long orderId, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("SELLER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"판매자 권한이 필요합니다."), ""));
            //throw new Exception403("판매자 권한이 필요합니다.");
        }

        Optional<OrderSheet> orderSheetOptional = orderSheetRepository.findById(orderId);

        if (!orderSheetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"주문 정보를 찾을 수 없습니다."), ""));
            //throw new Exception404("주문 정보를 찾을 수 없습니다.");
        }

        OrderSheet orderSheet = orderSheetOptional.get();
        orderSheet.setStatus(OrderStatus.CANCEL);

        //판매자는 어떤 주문이든 취소가능

        for (OrderProduct orderProduct : orderSheet.getOrderProductList()) {
            orderProduct.setStatus(OrderStatus.CANCEL);
            Product product = orderProduct.getProduct();
            product.addStock(orderProduct.getCount());
            productRepository.save(product);
        }

        //OrderSheet updatedOrderSheet = orderSheetRepository.save(orderSheet);

        ResponseDto<?> responseDto = new ResponseDto<>().data(orderSheet);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/customerplaceorder")
    public String customerPlaceOrder()
    {
        return "customerplaceorder";
    }

    @GetMapping("/customerorderlist")
    public String customerOrderList()
    {
        return "customerorderlist";
    }

    @GetMapping("/sellerorderlist")
    public String sellerOrderList()
    {
        return "sellerorderlist";
    }
}
