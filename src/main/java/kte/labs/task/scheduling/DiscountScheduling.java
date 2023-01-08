package kte.labs.task.scheduling;

import kte.labs.task.dto.ProductDto;
import kte.labs.task.services.ProductService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Data
@Component
public class DiscountScheduling {

    private final int DISCOUNT = 10;
    private final ProductService productService;

    @Scheduled(fixedDelay = 10000)
    public void schedulingDiscountOnProduct(){
        List<Long> list = productService.getAllProducts().stream().map(ProductDto::getId).collect(Collectors.toList());
        long idProduct = getRandomId(list);
        productService.setDiscountOnProduct(idProduct, DISCOUNT);
    }

    private static long getRandomId(List<Long> idList){
        int countProducts = idList.size();
        Random rn = new Random();
        int index = rn.nextInt(countProducts);
        return idList.get(index);
    }
}
