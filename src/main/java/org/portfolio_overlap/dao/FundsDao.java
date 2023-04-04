package org.portfolio_overlap.dao;

import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;

import java.util.Set;

public interface FundsDao {
    public Fund getFund(String name) throws FundNotFoundException;
    public Fund createFund(String name);
    public Fund updateFund(String name, Set<String> stocks) throws FundNotFoundException, StockNotFoundException;
    public Stock getStock(String name) throws StockNotFoundException;
    public Stock createStock(String name);
}
