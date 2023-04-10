package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.dto.order.OrderProductRequest;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final ProductRepository productRepository;

    /**
     * user가 주문
     * */
    @PostMapping("/save")
    public ResponseEntity<?> orderSave(@RequestBody OrderProductRequest.OrderSaveDto orderSaveDto){
        Product product = productRepository.findById(orderSaveDto.getProductId()).orElseThrow(
                () -> new Exception401("일치하는 상품 ID가 없습니다.")
        );
        OrderProduct orderProductPS = new OrderProduct();
        if (product.getQty() < orderSaveDto.getCount()){
            throw new Exception400("주문 개수가 상품의 재고보다 많습니다.");
        }else {
            orderSaveDto.setOrderPrice(product.getPrice() * orderSaveDto.getCount());
            product.setQty(product.getQty() - orderSaveDto.getCount());
            orderProductPS = orderProductRepository.save(orderSaveDto.toEntity(orderSaveDto, product));
            //jwt 공부 후에 user정보와 orderProduct 정보를 Sheet에 넣는 작업..
        }
        return new ResponseEntity<>(orderProductPS, HttpStatus.OK);
    }

    @DeleteMapping("/user/deleted/{id}")
    public void userDeletedOrder(@RequestBody Long id){
        //jwt 공부후 user인지 확인한 후 user 아이디 조회로 repository에 @qurey 작성
        orderProductRepository.deleteById(id);
        if (orderProductRepository.findById(id).isPresent()){
            throw new Exception401("삭제 실패 : id를 확인하세요");
        }
    }

    @DeleteMapping("/seller/deleted/{id}")
    public void sellerDeletedOrder(@PathVariable("id") Long id){
        orderProductRepository.deleteById(id);
        if (orderProductRepository.findById(id).isPresent()){
            throw new Exception401("삭제 실패 : id를 확인하세요");
        }
    }
    @GetMapping("/user/list")
    public ResponseEntity<?> userOrderList(@RequestBody String username){
        List<OrderSheet> orderSheetList = orderSheetRepository.findOrderSheetsByUserName(username);
        return new ResponseEntity<>(orderSheetList, HttpStatus.OK);
    }

    @GetMapping("/seller/list")
    public ResponseEntity<?> totalOrderList(){
        List<OrderProduct> orderProductList = orderProductRepository.findAll();
        return new ResponseEntity<>(orderProductList,HttpStatus.OK);
    }


    public void orderSheetSave(){

    }
}
