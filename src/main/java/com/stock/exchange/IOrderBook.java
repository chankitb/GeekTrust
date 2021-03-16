package com.stock.exchange;

import com.stock.exchange.model.TopBookEntry;
import com.stock.exchange.model.TradeRequest;

import java.util.Optional;

public interface IOrderBook {
    void addNewTradeInBook(TradeRequest tradeRequest);

    Optional<TopBookEntry> getTopBookEntry();

    void removeTopEntry();
}
