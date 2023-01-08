package kte.labs.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticsDto {

    int quantityRecipes;
    long totalCost;
    long amountDiscounts;

    public StatisticsDto(int quantityRecipes, long totalCost, long amountDiscounts) {
        this.quantityRecipes = quantityRecipes;
        this.totalCost = totalCost;
        this.amountDiscounts = amountDiscounts;
    }
}
