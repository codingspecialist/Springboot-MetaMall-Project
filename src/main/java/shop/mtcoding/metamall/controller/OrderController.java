package shop.mtcoding.metamall.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.annotation.Customer;
import shop.mtcoding.metamall.annotation.Permission;
import shop.mtcoding.metamall.core.CodeEnum;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.orderproduct.OrderProductRepository;
import shop.mtcoding.metamall.model.ordersheet.OrderSheet;
import shop.mtcoding.metamall.model.ordersheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    /**
     * 일단 OrderSheet(1) - OrderProduct(N) 인데 해당 구조는 문제가 많아보임 -> 여기서는 문제 없지만 User끼면 문제생김
     *    User(1) - OrderSheet(N) 이 구조가 문제인듯 함 해당 구조를 1-1 로 바꾸는게 나중에 마이페이지를 설계할 떄 좋다
     *      OneToMany를 두번 join fetch는 데이터를 튀기고 튀긴 데이터를 가져오기 때문에 기본적으로 여러번 쿼리가 일어나는 마이페이지에서는
     *      이런 구조가 성능에 발목을 잡을듯 하다.
     * */
    private final OrderProductRepository orderProductRepository;

    private final OrderSheetRepository orderSheetRepository;

    private final UserRepository userRepository;




    /**
     * @apiNote 소비자 권한으로만 들어올 수 있음 아닐 경우 403
     */

    @Customer
    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ResponseDto> save(@RequestBody List<OrderProduct> orderProductList, HttpServletRequest request){


       String username =  request.getAttribute("username").toString();


       OrderSheet data  = OrderSheet.builder()
               .userId(username)
               .totalPrice(orderProductList.stream().mapToInt(OrderProduct::getOrderPrice).sum())
               .build();

       data.addOrderProductList(orderProductList);

       OrderSheet orderSheet = orderSheetRepository.save(data);


        return ResponseEntity.status(CodeEnum.SUCCESS.getCode())
                .body(new ResponseDto().data(orderSheet).code(CodeEnum.SUCCESS).msg(CodeEnum.SUCCESS.getMessage()));

    }

    @Customer
    @GetMapping("/my")
    public ResponseEntity<ResponseDto> myPage(HttpServletRequest request){

        String username =  request.getAttribute("username").toString();
        log.info("MY PAGE {}",username);
        Optional<List<OrderSheet>> orderSheet =orderSheetRepository.findOrdData(username);
        List<OrderSheet> list= orderSheet.get();
        if(orderSheet.isPresent()){
            return ResponseEntity.status(CodeEnum.SUCCESS.getCode())
                    .body(new ResponseDto().data(list)
                            .code(CodeEnum.SUCCESS)
                            .msg(CodeEnum.SUCCESS.getMessage()));
        }else{
            return ResponseEntity.status(CodeEnum.SUCCESS.getCode())
                    .body(new ResponseDto().data(new ArrayList<>())
                            .code(CodeEnum.SUCCESS)
                            .msg(CodeEnum.SUCCESS.getMessage()));
        }
    }

    @Permission
    @GetMapping("/all/list")
    public  ResponseEntity<ResponseDto> getList(){
        Optional<List<OrderSheet>> orderSheet= orderSheetRepository.findAllData();
        ResponseDto<?> responseDto;


        if(orderSheet.isPresent()){
            List<OrderSheet> list = orderSheet.get();

            responseDto = new ResponseDto<>()
                    .data(list)
                    .code(CodeEnum.SUCCESS)
                    .msg(CodeEnum.SUCCESS.getMessage());
        }else{
            responseDto = new ResponseDto<>()
                        .data(new ArrayList<>())
                        .code(CodeEnum.SUCCESS)
                        .msg(CodeEnum.SUCCESS.getMessage());
        }

        return ResponseEntity.status(CodeEnum.SUCCESS.getCode()).body(responseDto);
    }


    /**
     * @apiNote 현재 로그인한 사용자에 정보를 받아와서 조회한 다음 삭제하고자하는 orderSheet PK를 찾아서 해당 그걸 삭제
     * @param 삭제하고자 하는 주문에 대한 PK 를 받는다.
     *              해당 PK로 데이터를 조회하고 해당 PK 데이터와 로그인한 사용자에 정보가 일치한다면 삭제하고 아니면 403을 리턴할 계획임
     *
     * */
    @Customer
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> delete(Long id,HttpServletRequest request){

        String username =  request.getAttribute("username").toString();
        String role =  request.getAttribute("role").toString();
        Optional<OrderSheet> orderSheet = orderSheetRepository.findByIdWithUser(id);

        if(orderSheet.isPresent()){
            OrderSheet orderSheet1 = orderSheet.get();
            if(orderSheet1.getUser().getUsername().equals(username) || role.equals("SELLER")){
                orderSheetRepository.delete(orderSheet1);
                return ResponseEntity
                        .status(CodeEnum.SUCCESS.getCode())
                        .body(new ResponseDto()
                                .code(CodeEnum.SUCCESS)
                                .data(orderSheet)
                                .msg(CodeEnum.SUCCESS.getMessage()));
            }else{
                return ResponseEntity
                        .status(CodeEnum.FORBIDDEN.getCode())
                        .body(new ResponseDto().data(orderSheet)
                                .code(CodeEnum.FORBIDDEN)
                                .msg(CodeEnum.FORBIDDEN.getMessage()));
            }
        }else{
            return ResponseEntity
                    .status(CodeEnum.NOT_FOUND.getCode())
                    .body(new ResponseDto()
                            .code(CodeEnum.NOT_FOUND)
                            .msg(CodeEnum.NOT_FOUND.getMessage()));
        }
    }

    /**
     * @apiNote admin 관리자가 해당 리소스에만 접근가능하고 id값만 있다면 어떤 주문이든 다 삭제 가능
     *
     * */
    @Permission
    @DeleteMapping("/admin/delete")
    public ResponseEntity<ResponseDto> deleteAmin(Long id,HttpServletRequest request){
        Optional<OrderSheet> orderSheetOptional = orderSheetRepository.findById(id);

        if(orderSheetOptional.isPresent()){
            OrderSheet data = orderSheetOptional.get();
            orderSheetRepository.delete(data);

            return ResponseEntity
                    .status(CodeEnum.SUCCESS.getCode())
                    .body(new ResponseDto().data(data)
                            .msg(CodeEnum.SUCCESS.getMessage()));
        }else{
            return ResponseEntity
                    .status(CodeEnum.NOT_FOUND.getCode())
                    .body(new ResponseDto()
                            .code(CodeEnum.NOT_FOUND)
                            .msg(CodeEnum.NOT_FOUND.getMessage()));
        }
    }
}

