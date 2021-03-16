package com.stock.exchange;

import com.google.common.base.Splitter;
import com.opencsv.CSVReader;
import com.stock.exchange.model.TradeRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Scanner;

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
            Path path = Paths.get(inputFilePath);
            Scanner scanner = new Scanner(path);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                Iterable<String> split = Splitter.on(' ')
                        .omitEmptyStrings()
                        .limit(6)
                        .split(line);
                Iterator<String> iterator = split.iterator();
                TradeRequest tradeRequest = TradeRequest.builder()
                        .orderId(iterator.next())
                        .time(df.parse(iterator.next()))
                        .stock(iterator.next())
                        .type(TradeRequest.Type.valueOf(iterator.next()))
                        .price(Double.valueOf(iterator.next()))
                        .qty(Integer.valueOf(iterator.next())).build();

                tradeController.process(tradeRequest);
            }
            scanner.close();
        } catch (IOException | ParseException e) {
            log.error("Error in parsing the input file, Only single space is allowed as separator" + e.getMessage());
        }
    }
}
