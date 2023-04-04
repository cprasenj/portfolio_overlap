package org.portfolio_overlap.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Fund {
    private Set<Stock> stocks;

    public String getFundName() {
        return fundName;
    }

    private String fundName;

    public Fund(String fundName, Set<Stock> stocks) {
        this.stocks = stocks;
        this.fundName = fundName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fund fund = (Fund) o;
        return Objects.equals(stocks, fund.stocks) && Objects.equals(fundName, fund.fundName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stocks, fundName);
    }

    public Fund(Fund that) {
        this(that.getFundName(), new HashSet<>(that.getStocks()));
    }

    public Set<Stock> getStocks() {
        return this.stocks;
    }

    public Fund addStocks(Set<Stock> new_stocks) {
        this.stocks.addAll(new_stocks);
        return new Fund(this);
    }

    public Set<Stock> intersection(Fund that) {
        Set<Stock> intersection = new HashSet<>(this.getStocks());
        intersection.retainAll(that.getStocks());
        return intersection;
    }
    public Set<Stock> stocksDoesNotBelongsTo(Fund that) {
        Set<Stock> stocksOfThis = new HashSet<>(this.getStocks());
        stocksOfThis.removeAll(that.getStocks());
        return stocksOfThis;
    }

    @Override
    public String toString() {
        return fundName;
    }
}
