package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.dto.ResponseDto;
import shop.mtcoding.metamall.dto.product.ProductRequest;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@PostMapping("/products")
	public ResponseEntity<ResponseDto> productsSave(@RequestBody ProductRequest.ProductSaveDto productDto, HttpServletRequest request ){
		Optional<Product> productOP = productRepository.findByName(productDto.getName());
		if(!productOP.isPresent()){
			Product product = Product.builder()
							.name(productDto.getName())
							.price(productDto.getPrice())
							.qty(productDto.getQty())
							.build();
			productRepository.save(product);
			ResponseDto<?> responseDto = new ResponseDto<>().data(product);
			return ResponseEntity.ok().body(responseDto);
		}else{
			throw new Exception400 ("중복된 상품명이 있습니다.");
		}
	}

	@GetMapping("/products")
	public ResponseEntity<ResponseDto> productList(){
		List<Product> productList=productRepository.findAll();
		ResponseDto<?> responseDto=new ResponseDto<>().data(productList);
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/products/{name}")
	public ResponseEntity<ResponseDto> productDetail(@PathVariable String name){
		Optional<Product> product=productRepository.findByName(name);
		if(product.isPresent()){
			ResponseDto<?> responseDto = new ResponseDto<>().data(product);
			return ResponseEntity.ok().body(responseDto);
		}else{
			throw new Exception400 ("존재하지 않는 상품입니다. 상품명을 다시 입력하세요 ");
		}
	}

	/***
	 *
	 * @param productDto
	 * @return 상품수정
	 */
	@PutMapping("/products/{name}")
	public ResponseEntity<ResponseDto> productUpdate(@RequestBody ProductRequest.ProductUpdateDto productDto){
		Optional<Product>product=productRepository.findByName(productDto.getName());
		if(product.isPresent()){
			product.get().updateProduct(productDto);
			productRepository.save(product.get());
			ResponseDto<?> responseDto = new ResponseDto<>().data(product);
			return ResponseEntity.ok().body(responseDto);
		}else{
			throw new Exception400 ("존재하지 않는 상품입니다. 상품명을 다시 입력하세요 ");
		}
	}

	@Transactional
	@DeleteMapping("/products/{name}")
	public ResponseEntity<ResponseDto> productDelete(@PathVariable String name){
		productRepository.deleteByName(name);
		ResponseDto<?>responseDto=new ResponseDto<>().data("삭제완료");
		return ResponseEntity.ok().body(responseDto);

	}




}
