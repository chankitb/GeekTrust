package com.stock.exchange;

import com.opencsv.CSVReader;
import com.stock.exchange.model.TradeRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by chankit.bansal on 16/03/21.
 */
@Slf4j
public class Application {

    private static final SimpleDateFormat df = new SimpleDateFormat("hh:mm");
    private final TradeController tradeController;

    public Application() {
        this.tradeController = new TradeController();
    }


    public static void main(String[] args) {
        if(args.length >= 1) {
            String inputPath = args[0];
            Application application = new Application();
            application.ReadAndProcessOrder(inputPath);
        }else {
            log.error("Program expect input file path");
        }

    }

    private void ReadAndProcessOrder(String inputFilePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(inputFilePath), ' ');
            String[] line;
            while ((line = reader.readNext()) != null) {
                TradeRequest tradeRequest = TradeRequest.builder()
                        .orderId(line[0])
                        .time(df.parse(line[1]))
                        .stock(line[2])
                        .type(TradeRequest.Type.valueOf(line[3]))
                        .price(Double.valueOf(line[4]))
                        .qty(Integer.valueOf(line[5])).build();
                tradeController.process(tradeRequest);
            }
        } catch (IOException | ParseException e) {
            log.error("Error in parsing the input file, Only single space is allowed as separator" + e.getMessage());
        }
    }
}
