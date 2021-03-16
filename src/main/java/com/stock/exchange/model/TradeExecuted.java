package com.stock.exchange.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by chankit.bansal on 16/03/21.
 */

@Data
@Builder
public class TradeExecuted {
    private String buyOrderId;
    private Double sellPrice;
    private Integer qty;
    private String sellOrderId;
}
