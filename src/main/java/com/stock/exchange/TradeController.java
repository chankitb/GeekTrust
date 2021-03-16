package com.stock.exchange;

import com.stock.exchange.model.TradeExecuted;
import com.stock.exchange.model.TradeRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by chankit.bansal on 16/03/21.
 */

@Slf4j
public class TradeController {

    private final IOrderBook IOrderBook;

    public TradeController() {
        this.IOrderBook = new OrderBook();

    }

    public void process(TradeRequest tradeRequest){
        IOrderBook.addNewTradeInBook(tradeRequest);
        final AtomicReference<Boolean> pendingExecution = new AtomicReference<>(true);

        while(pendingExecution.get() && IOrderBook.getTopBookEntry().isPresent()) {
            IOrderBook.getTopBookEntry().ifPresent(topBookEntry -> {
                TradeRequest buyTrade = topBookEntry.getBuyRequest();
                TradeRequest sellTrade = topBookEntry.getSellRequest();
                if (buyTrade.getPrice() > sellTrade.getPrice()) {
                    IOrderBook.removeTopEntry();
                    executeTrade(buyTrade, sellTrade);
                } else {
                    pendingExecution.set(false);
                }

            });
        }
    }

    private void executeTrade(TradeRequest buyTrade, TradeRequest sellTrade) {
        TradeExecuted.TradeExecutedBuilder tradeExecutedBuilder = TradeExecuted.builder()
                .buyOrderId(buyTrade.getOrderId())
                .sellOrderId(sellTrade.getOrderId())
                .sellPrice(sellTrade.getPrice());

        if (buyTrade.getQty() > sellTrade.getQty()) {
            tradeExecutedBuilder.qty(sellTrade.getQty());
            buyTrade.setQty(buyTrade.getQty() - sellTrade.getQty());
            IOrderBook.addNewTradeInBook(buyTrade);
        } else if (sellTrade.getQty() > buyTrade.getQty()) {
            tradeExecutedBuilder.qty(buyTrade.getQty());
            sellTrade.setQty(sellTrade.getQty() - buyTrade.getQty());
            IOrderBook.addNewTradeInBook(sellTrade);
        } else
            tradeExecutedBuilder.qty(buyTrade.getQty());

        TradeExecuted tradeExecuted = tradeExecutedBuilder.build();
        pushToConsole(tradeExecuted);
    }

    private void pushToConsole(TradeExecuted tradeExecuted){
        String[] trade = new String[] { tradeExecuted.getBuyOrderId(), String.format("%.2f", tradeExecuted.getSellPrice()),
                tradeExecuted.getQty().toString(), tradeExecuted.getSellOrderId() };

        System.out.println(Arrays.toString(trade)
                                   .replace(",", "")
                                   .replace("[", "")
                                   .replace("]", "").trim());
    }

}
