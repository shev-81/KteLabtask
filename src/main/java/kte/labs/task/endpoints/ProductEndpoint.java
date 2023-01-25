package kte.labs.task.endpoints;

import kte.labs.task.services.ProductService;
import kte.labs.task.soap.products.GetAllProductsRequest;
import kte.labs.task.soap.products.GetAllProductsResponse;
import kte.labs.task.soap.products.GetProductByIdRequest;
import kte.labs.task.soap.products.GetProductByIdResponse;
import kte.labs.task.soap.products.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {
    private static final String NAMESPACE_URI = "http://www.user.com/spring/ws/products";
    private final ProductService productService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductByIdRequest")
    @ResponsePayload
    @Transactional
    public GetProductByIdResponse getProductById(@RequestPayload GetProductByIdRequest request) {
        GetProductByIdResponse response = new GetProductByIdResponse();
        long productId = request.getId();
        Product productDto = new Product();
        kte.labs.task.entities.Product product = productService.getProductById(productId);
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice().intValue());
        productDto.setDiscount(product.getDiscount());
        response.setProduct(productDto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {
        GetAllProductsResponse response = new GetAllProductsResponse();
        productService.getAllProducts().stream().map(P -> {
            Product productDto = new Product();
            productDto.setId(P.getId());
            productDto.setTitle(P.getTitle());
            productDto.setDescription(P.getDescription());
            productDto.setPrice(P.getPrice().intValue());
            productDto.setDiscount(P.getDiscount());
            return productDto;
        }).forEach(response.getProducts()::add);
        return response;
    }
}
