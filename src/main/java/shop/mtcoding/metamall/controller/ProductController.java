package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception401;
import shop.mtcoding.metamall.core.exception.Exception403;
import shop.mtcoding.metamall.core.exception.Exception404;
import shop.mtcoding.metamall.core.jwt.JwtProvider;
import shop.mtcoding.metamall.core.session.LoginUser;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.log.error.ErrLogService;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ProductController
{
    private final ProductRepository productRepository;

    private final ErrLogService err;

    @ResponseBody
    @Transactional
    @PostMapping("/item/register")
    public ResponseEntity<?> register(@RequestBody Product product, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (!loginUser.getRole().equals("SELLER")) {
            //throw new Exception403("판매자만 상품을 등록할 수 있습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"판매자만 상품을 등록할 수 있습니다"), ""));
        }

        Product registeredProduct = productRepository.save(product);

        ResponseDto<?> responseDto = new ResponseDto<>().data(registeredProduct);

        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(HttpServletRequest request) {

        List<Product> products = productRepository.findAll();

        ResponseDto<?> responseDto = new ResponseDto<>().data(products);
        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getItemDetail(@PathVariable("itemId") Long itemId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        Optional<Product> productOptional = productRepository.findById(itemId);

        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"상품을 찾을 수 없습니다"), ""));
           // throw new Exception404("상품을 찾을 수 없습니다");
        }

        Product product = productOptional.get();
        ResponseDto<?> responseDto = new ResponseDto<>().data(product);
        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @Transactional
    @PutMapping("/item/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("itemId") Long itemId,
                                        @RequestBody Product updatedProduct,
                                        HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (!loginUser.getRole().equals("SELLER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"판매자만 상품을 수정 할 수 있습니다"), ""));
        }

        Optional<Product> productOptional = productRepository.findById(itemId);

        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"상품을 찾을 수 없습니다"), ""));
        }

        Product product = productOptional.get();

        // 상품 정보 수정
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setQty(updatedProduct.getQty());

        // 수정된 상품 정보 저장
        Product savedProduct = productRepository.save(product);
        ResponseDto<?> responseDto = new ResponseDto<>().data(savedProduct);
        return ResponseEntity.ok().body(responseDto);
    }

    @ResponseBody
    @Transactional
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") Long itemId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (!loginUser.getRole().equals("SELLER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"판매자만 상품을 삭제할 수 있습니다."), ""));
            //throw new Exception403("판매자만 상품을 삭제할 수 있습니다.");
        }

        Optional<Product> productOptional = productRepository.findById(itemId);

        if (!productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>().fail(HttpStatus.BAD_REQUEST, err.log(loginUser.getId(),"상품을 찾을 수 없습니다"), ""));
            //throw new Exception404("상품을 찾을 수 없습니다");
        }

        Product product = productOptional.get();
        productRepository.delete(product);

        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/addproduct")
    public String addProduct()
    {
        return "addproduct";
    }

    @GetMapping("/editproduct")
    public String editProduct()
    {
        return "editproduct";
    }

    @GetMapping("/deleteproduct")
    public String deleteProduct()
    {
        return "deleteproduct";
    }

    @GetMapping("/productlist")
    public String productList()
    {
        return "productlist";
    }
}
