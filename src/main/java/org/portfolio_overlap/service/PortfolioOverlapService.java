package org.portfolio_overlap.service;

import org.portfolio_overlap.dao.FundsDao;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PortfolioOverlapService {
    private FundsDao fundsDao;

    public PortfolioOverlapService(FundsDao fundsDao) {
        this.fundsDao = fundsDao;
    }

    public float fundOverlap(Fund one, Fund another) throws FundNotFoundException {
        Set<Stock> intersection = one.intersection(another);
        return (((float) (2 * intersection.size())) / (one.getStocks().size() + another.getStocks().size())) * 100;
    }
     public Fund addStockToExistingFund(Fund fund, Stock stock) throws StockNotFoundException, FundNotFoundException {
        return this.fundsDao.updateFund(fund.getFundName(), new HashSet<>(Collections.singletonList(stock.getName())));
     }

     public Fund getFundByName(String fundName) throws FundNotFoundException {
        return this.fundsDao.getFund(fundName);
     }

    public Stock getStockByName(String stockName) throws StockNotFoundException {
        return this.fundsDao.getStock(stockName);
    }

    public Stock addStock(Stock stock) {
        return this.fundsDao.createStock(stock.getName());
    }

}
