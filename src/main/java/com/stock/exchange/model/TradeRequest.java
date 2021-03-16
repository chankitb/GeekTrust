package com.stock.exchange.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by chankit.bansal on 16/03/21.
 */

@Data
@Builder
@EqualsAndHashCode
public class TradeRequest {
    private String orderId;
    private Date time;
    private String stock;
    private Type type;
    private Double price;
    private Integer qty;

    public enum Type{
        buy,
        sell
    }
}
