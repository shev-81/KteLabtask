package kte.labs.task.controllers;

import kte.labs.task.dto.ClientDto;
import kte.labs.task.dto.QueryProductDto;
import kte.labs.task.dto.ProductDto;
import kte.labs.task.dto.ProductInfoDto;
import kte.labs.task.dto.StatisticsDto;
import kte.labs.task.services.ClientService;
import kte.labs.task.services.ProductService;
import kte.labs.task.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/market")
public class MarketController {

    private final ProductService productService;
    private final ClientService clientService;
    private final SaleService saleService;

    /**
     * Cписок клиентов (все атрибуты).
     * @return
     */
    @GetMapping("/clients")
    public List<ClientDto> clientList(){
        return clientService.getAllClients();
    }

    /**
     * Cписок товаров (идентификатор, наименование, цена).
     * @return
     */
    @GetMapping("/products")
    public List<ProductDto> productList(){
        return productService.getAllProducts();
    }

    /**
     * Изменение индивидуальных скидок клиента (входные параметры: идентификатор, скидка 1, скидка 2).
     * @param id
     * @param discont1
     * @param discont2
     * @return
     */
    @GetMapping("/userdiscont/{id}/{discont1}/{discont2}")
    public boolean discont(@PathVariable long id,
                           @PathVariable long discont1,
                           @PathVariable long discont2){
        return clientService.setDiscont(id, discont1, discont2);
    }

    /**
     * Получение дополнительной информации о товаре
     * @param productId
     * @param clientId
     * @return
     */
    @GetMapping("/productInfo/{productId}/{clientId}")
    public ProductInfoDto productInfo(@PathVariable long productId,
                                      @PathVariable long clientId){
        return saleService.getProductInfo(productId, clientId);
    }

    /**
     * Запрос итоговой стоимости
     * вх. параметры:
     * - идентификатор клиента
     * - парами: идентификатор товара,  количество.
     * вых. параметры:
     * - итоговая стоимость с учетом скидок (в копейках).
     * @param list
     * @param clientId
     * @return
     */
    @PostMapping("/totalcost/{clientId}")
    public BigDecimal productTotalCost(@RequestBody List<QueryProductDto> list,
                                       @PathVariable long clientId){
        return productService.getTotalCost(list, clientId);
    }

    /**
     * Регистрация продажи
     * вх. параметры:
     * - парами: идентификатор товара, количество;
     * - итоговая стоимость с учетом скидок (в копейках).
     * вых. параметры:
     * - номер чека.
     * Ошибка в том случае, если переданная итоговая стоимость не соответствует рассчитанной на момент регистрации продажи.
     * @param list
     * @param clientId
     * @param totalPriceWithDiscount
     * @return
     */
    @PostMapping("/sale/{clientId}/{totalPriceWithDiscount}")
    public String registrationOfSale(@RequestBody List<QueryProductDto> list,
                                     @PathVariable long clientId,
                                     @PathVariable long totalPriceWithDiscount){
        return saleService.getReceipt(list, clientId, totalPriceWithDiscount);
    }

    /**
     * Оценка товара
     * вх. параметры:
     * - идентификатор клиента;
     * - идентификатор товара;
     * - оценка (1-5 или null для отзыва оценки).  ProductEvaluation
     * @param clientId
     * @param productId
     * @param evaluation
     * @return
     */
    @GetMapping("/evaluation/{clientId}/{productId}/{evaluation}")
    public boolean registrationOfSale(@PathVariable long clientId,
                                      @PathVariable long productId,
                                      @PathVariable long evaluation){
        return productService.evaluationProduct(clientId, productId, evaluation);
    }

    /**
     * Получение статистики
     * вх. параметры:
     * - идентификатор клиента;
     * - идентификатор товара;
     * Может быть передан только один.
     * вых. параметры:
     * - кол-во чеков;
     * - общая стоимость (для клиента - чеков, для товаров - соотв. позиций) по исходной цене;
     * - сумма скидок (для клиента - по всем
     * @param clientId
     * @param productId
     * @return
     */
    @GetMapping("/statistics/{clientId}/{productId}")
    public StatisticsDto registrationOfSale(@PathVariable(required = false) Long clientId,
                                            @PathVariable(required = false) Long productId){
        return saleService.getStatistics(clientId, productId);
    }

}
