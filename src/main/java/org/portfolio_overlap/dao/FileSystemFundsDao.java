package org.portfolio_overlap.dao;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileSystemFundsDao implements FundsDao {
    private Map<String, Fund> funds = new HashMap<>();
    private Map<String, Stock> stocks = new HashMap<>();

    private void initStock(String stockData) {
        try {
            List<Map<String, Object>> funds = (List<Map<String, Object>>) (new ObjectMapper().readValue(stockData, Map.class)).get("funds");
            for (Map<String, Object> fundData: funds){
                Set<Stock> stocks = new HashSet<>();
                String fundName = (String) fundData.get("name");
                List<String> stockNames = (List<String>) fundData.get("stocks");
                for (String stockName: stockNames) {
                    Stock stock = new Stock(stockName.toUpperCase());
                    this.stocks.put(stockName.toUpperCase(), stock);
                    stocks.add(new Stock(stock));
                }
                Fund fund = new Fund(fundName.toUpperCase(), new HashSet<>(stocks));
                this.funds.put(fundName.toUpperCase(), fund);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public FileSystemFundsDao(String filename) throws IOException {
        String default_filename = "data/funds.json";
        if (filename == null) {
            filename = default_filename;
        }
        this.initStock(new String(Files.readAllBytes(Paths.get(filename))));
    }

    @Override
    public Fund getFund(String name) throws FundNotFoundException {
        Fund fund = null;
        if(name != null) {
            fund = this.funds.get(name.toUpperCase());
        }
        if (fund == null) {
            throw new FundNotFoundException("Fund with name " + name + " not present");
        }
        return new Fund(fund);
    }

    @Override
    public Fund createFund(String name) {
        return null;
    }

    @Override
    public Fund updateFund(String name, Set<String> stocks) throws FundNotFoundException, StockNotFoundException {
        Fund fund = this.funds.get(name.toUpperCase());
        Set<Stock> stocksToBeAdded = new HashSet<>();
        if (fund == null) {
            throw new FundNotFoundException("Fund with name " + name + " not present");
        }
        for (String stockName: stocks) {
            Stock stockLookup = this.stocks.get(stockName.toUpperCase());
            if (stockLookup == null) {
                throw new StockNotFoundException("Stock with name " + stockName + " not present");
            }
            stocksToBeAdded.add(stockLookup);
        }
        Fund copyFund = new Fund(fund);
        copyFund.addStocks(stocksToBeAdded);
        this.funds.put(name.toUpperCase(), new Fund(copyFund));
        return copyFund;
    }

    @Override
    public Stock getStock(String name) throws StockNotFoundException {
        Stock stock = null;
        if(name != null) {
            stock = this.stocks.get(name.toUpperCase());
        }
        if (stock == null) {
            throw new StockNotFoundException("Stock with name " + name + " not present");
        }
        return new Stock(stock);
    }

    @Override
    public Stock createStock(String name) {
        Stock stock = this.stocks.get(name.toUpperCase());
        if (stock != null) {
            return stock;
        }
        Stock new_stock = new Stock(name.toUpperCase());
        this.stocks.put(name.toUpperCase(), new_stock);
        return new Stock(new_stock);
    }

}
