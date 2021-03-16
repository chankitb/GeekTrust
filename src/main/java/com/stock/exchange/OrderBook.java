package com.stock.exchange;

import com.google.common.collect.Sets;
import com.stock.exchange.model.TopBookEntry;
import com.stock.exchange.model.TradeRequest;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Created by chankit.bansal on 16/03/21.
 */
public class OrderBook implements IOrderBook {

    private Comparator customBuyComparator = (Comparator) (o1, o2) -> {
        if (((TradeRequest)o1).getPrice() > ((TradeRequest)o2).getPrice()){
            return -1;
        }else if (((TradeRequest) o1).getPrice().equals(((TradeRequest) o2).getPrice())){
            return compareDate((TradeRequest) o1, (TradeRequest) o2);
        }else
            return 1;

    };

    private Comparator customSellComparator = (Comparator) (o1, o2) -> {
        if (((TradeRequest)o1).getPrice() > ((TradeRequest)o2).getPrice()){
            return 1;
        }else if (((TradeRequest) o1).getPrice().equals(((TradeRequest) o2).getPrice())){
            return compareDate((TradeRequest) o1, (TradeRequest) o2);
        }else
            return -1;

    };

    private TreeSet<TradeRequest> buyBook = Sets.newTreeSet(customBuyComparator);
    private TreeSet<TradeRequest> sellBook= Sets.newTreeSet(customSellComparator);

    @Override
    public void addNewTradeInBook(TradeRequest tradeRequest){
        if (tradeRequest.getType().equals(TradeRequest.Type.buy)){
            buyBook.add(tradeRequest);
        } else
            sellBook.add(tradeRequest);
    }

    @Override
    public Optional<TopBookEntry> getTopBookEntry(){
        if(!buyBook.isEmpty() && !sellBook.isEmpty()) {
            return Optional.of(TopBookEntry.builder().buyRequest(buyBook.first())
            .sellRequest(sellBook.first()).build());
        }
        return Optional.empty();
    }

    @Override
    public void removeTopEntry(){
        buyBook.pollFirst();
        sellBook.pollFirst();
    }


    private int compareDate(TradeRequest t1, TradeRequest t2){
        return t1.getTime().compareTo(t2.getTime());
    }
}
