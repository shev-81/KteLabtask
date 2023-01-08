package kte.labs.task.services;

import kte.labs.task.dto.ClientDto;
import kte.labs.task.dto.PositionDto;
import kte.labs.task.dto.QueryProductDto;
import kte.labs.task.dto.ProductInfoDto;
import kte.labs.task.dto.StatisticsDto;
import kte.labs.task.entities.Position;
import kte.labs.task.entities.Product;
import kte.labs.task.entities.Rating;
import kte.labs.task.entities.Sale;
import kte.labs.task.exceptions.ResourceNotFoundException;
import kte.labs.task.repositories.PositionRepository;
import kte.labs.task.repositories.SaleRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final PositionRepository positionRepository;
    private final ProductService productService;
    private final ClientService clientService;

    @Transactional
    public ProductInfoDto getProductInfo(long productId, long  clientId){
        List<Sale> listSales = saleRepository.findAllByClientId(clientId);
        if(listSales.size() == 0){
            throw new ResourceNotFoundException("Not sales found for client: " + clientId);
        }
        Product product = productService.getProductById(productId);
        List<Rating> listRating = product.getRating();
        return new ProductInfoDto(product.getDescription(), averageRating(listRating));
    }

    @Transactional
    public String getReceipt(List<QueryProductDto> list, long  clientId, long totalPriceWithDiscount){
        ClientDto clientDto = clientService.getClientById(clientId);
        BigDecimal totalPriceFromQuery = new BigDecimal(totalPriceWithDiscount);
        long individualDiscount = ClientService.getIndividualDiscount(clientDto, list.size());
        BigDecimal totalPriceInSystem = ProductService.priceWithDiscountPercents(productService.calculateTotalPrice(list, clientDto), new BigDecimal(individualDiscount));
        if(!totalPriceFromQuery.equals(totalPriceInSystem)){
            throw new RuntimeException("The transferred full price differs from the full price in the system");
        }
        List<Long> productIds = ProductService.productIdList(list);
        List<Product> productList = productService.getProducts(productIds);
        Map<Long, Product> mapProduct = Product.makeMap(productList);
        List<Position> positionList = new ArrayList<>();
        for(QueryProductDto product: list){
            BigDecimal price = mapProduct.get(product.getProductId()).getPrice();
            Position position = new Position(
                    product.getProductId(),
                    product.getQuantity(),
                    price,
                    ProductService.priceWithDiscountPercents(price,new BigDecimal(individualDiscount)),
                    individualDiscount);
            positionList.add(position);
        }
        positionRepository.saveAll(positionList);
        Sale sale = new Sale(clientId, new Date(), positionList);
        Sale sale1 = saleRepository.save(sale);
        return "00" + sale1.getReceiptNumber();
    }

    public StatisticsDto getStatistics(Long clientId, Long productId){
        List<Sale> sales = saleRepository.findAllByClientId(clientId);
        List<PositionDto> positionList = Sale.listPositions(sales);
        if(productId != null){
            positionList = positionList.stream().filter(P -> (P.getProductId() == productId)).collect(Collectors.toList());
        }
        long totalCost = positionList.stream().map(PositionDto::getOriginalPrice).mapToLong(i -> i).sum();
        long amountDiscounts = positionList.stream().map(PositionDto::getFinalPrice).mapToLong(i -> i).sum();
        return new StatisticsDto(sales.size(), totalCost, amountDiscounts);
    }

    private static long averageRating(List<Rating> listRating){
        int count = listRating.size();
        long sumRatings = listRating.stream().map(Rating::getEvaluation).mapToLong(i -> i).sum();
        return sumRatings / count;
    }
}
