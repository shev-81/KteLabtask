package kte.labs.task.services;

import kte.labs.task.dto.ClientDto;
import kte.labs.task.dto.QueryProductDto;
import kte.labs.task.dto.ProductDto;
import kte.labs.task.entities.Product;
import kte.labs.task.entities.Rating;
import kte.labs.task.exceptions.ResourceNotFoundException;
import kte.labs.task.repositories.ProductRepository;
import kte.labs.task.repositories.RatingRepository;
import lombok.Data;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class ProductService {

    private final int TOTAL_DISCOUNT_PERCENT = 18;
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;
    private final ClientService clientService;

    public List<ProductDto> getAllProducts(){
        List<Product> list = productRepository.findAll();
        return Product.listDto(list);
    }

    public List<Product> getProducts(List<Long> productIds){
        return  productRepository.findAllById(productIds);
    }

    public Product getProductById(long productId){
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not fount by id."));
    }

    @Transactional
    public BigDecimal getTotalCost(List<QueryProductDto> list, @PathVariable long clientId){
        ClientDto clientDto = clientService.getClientById(clientId);
        long individualDiscount = ClientService.getIndividualDiscount(clientDto, list.size());
        return priceWithDiscountPercents(calculateTotalPrice(list, clientDto), new BigDecimal(individualDiscount));
    }

    @Transactional
    public boolean evaluationProduct(long clientId, long productId, long evaluation){
        Product product = getProductById(productId);
        Rating rating = new Rating(clientId, evaluation, product);
        ratingRepository.save(rating);
        return true;
    }

    @Transactional
    public void setDiscountOnProduct(Long productId, int discount){
        Product product = getProductById(productId);
        product.setDiscount(discount);
    }

    public BigDecimal calculateTotalPrice(List<QueryProductDto> list, ClientDto client){
        List<Long> productIds = productIdList(list);
        List<Product> productList = productRepository.findAllById(productIds);
        BigDecimal totalPrice = new BigDecimal(0);
        long discountClient = ClientService.getIndividualDiscount(client, productList.size());
        for(Product P: productList) {
            if(P.getDiscount() + discountClient > TOTAL_DISCOUNT_PERCENT){
                throw new RuntimeException("The discount amount cannot exceed 18%");
            }
            if(P.getDiscount() > 0){
                BigDecimal discountPr = new BigDecimal(P.getDiscount());
                BigDecimal discountCl = new BigDecimal(discountClient);
                BigDecimal fullDiscount = discountPr.add(discountCl);
                totalPrice = totalPrice.add(priceWithDiscountPercents(P.getPrice(), fullDiscount));
                continue;
            }
            totalPrice = totalPrice.add(P.getPrice());
        }
        return totalPrice;
    }

    public static BigDecimal priceWithDiscountPercents(BigDecimal base, BigDecimal pct){
        BigDecimal tmpDiscount = base.divide(new BigDecimal(100)).multiply(pct);
        return base.subtract(tmpDiscount);
    }

    public static List<Long> productIdList(List<QueryProductDto> list){
        return list.stream().map(QueryProductDto::getProductId).collect(Collectors.toList());
    }
}
