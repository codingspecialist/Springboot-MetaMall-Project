package shop.mtcoding.metamall.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.annotation.BindingCheck;
import shop.mtcoding.metamall.annotation.Permission;
import shop.mtcoding.metamall.core.CodeEnum;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;



    @GetMapping("/list")
    public ResponseEntity<ResponseDto> list(@PageableDefault(sort = "id",
                                            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request){
        Page<Product> pageList =productRepository.findAll(pageable);
        PageRequest<Product> pageRequest = new PageRequest<Product>(pageList.getTotalPages(),
                                                            pageList.getNumber(),
                                                            pageList.getContent());
        ResponseDto<?> responseDto = new ResponseDto<>()
                .data(pageRequest)
                .code(CodeEnum.SUCCESS)
                .msg("SUCCESS");

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> detail(@PathVariable Long id){
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()){
            Product entity =product.get();
            ResponseDto<?> dto =new ResponseDto<>()
                    .data(entity)
                    .code(CodeEnum.SUCCESS)
                    .msg(CodeEnum.SUCCESS.getMessage());

            return ResponseEntity.ok(dto);
        }else{
            ResponseDto<?> dto =new ResponseDto<>()
                    .code(CodeEnum.NOT_FOUND)
                    .msg(CodeEnum.NOT_FOUND.getMessage());

            return ResponseEntity.status(CodeEnum.NOT_FOUND.getCode()).body(dto);
        }
    }

    @Permission
    @BindingCheck
    @PostMapping("/save")
    public ResponseEntity<ResponseDto> save(@RequestBody @Valid RequestProductDTO productDTO,
                                            BindingResult bindingResult){

        productRepository.save(toEntity(productDTO));
        System.out.println(productRepository);
        return ResponseEntity.ok(new ResponseDto()
                .code(CodeEnum.SUCCESS)
                .msg(CodeEnum.SUCCESS.getMessage())
                .data(true));
    }

    @Permission
    @BindingCheck
    @Transactional
    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modify(@RequestBody @Valid RequestProductDTO productDTO){
        Optional<Product> product = productRepository.findById(productDTO.getId());

        if(product.isPresent()){
            Product entity = product.get();
            entity.changeProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getQty());

            ResponseDto<?> dto =new ResponseDto<>()
                    .data(entity)
                    .code(CodeEnum.SUCCESS)
                    .msg(CodeEnum.SUCCESS.getMessage());

            return ResponseEntity.ok(dto);

        }else{

            ResponseDto<?> dto = new ResponseDto<>()
                    .code(CodeEnum.INVALID_ARGUMENT)
                    .msg(CodeEnum.INVALID_ARGUMENT.getMessage());

            return ResponseEntity.status(CodeEnum.INVALID_ARGUMENT.getCode()).body(dto);
        }
    }

    @Permission
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id){
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()){
            Product entity =product.get();
            productRepository.delete(entity);
            ResponseDto<?> dto =new ResponseDto<>()
                    .data(entity)
                    .code(CodeEnum.SUCCESS)
                    .msg(CodeEnum.SUCCESS.getMessage());

            return ResponseEntity.ok(dto);
        }else{
            ResponseDto<?> dto =new ResponseDto<>()
                    .code(CodeEnum.NOT_FOUND)
                    .msg(CodeEnum.NOT_FOUND.getMessage());

            return ResponseEntity.status(CodeEnum.NOT_FOUND.getCode()).body(dto);
        }

    }

    protected Product toEntity(RequestProductDTO dto){
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .qty(dto.getQty())
                .build();
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class RequestProductDTO{

        private Long id;
        @NotEmpty
        private String name; // 상품 이름
        private Integer price; // 상품 가격

        @Positive //0보다커야함
        private Integer qty; // 상품 재고

    }

    @Data
    @Builder
    static class PageRequest<T>{
        int total;
        int nowPage;

        List<T> data;



    }
}
