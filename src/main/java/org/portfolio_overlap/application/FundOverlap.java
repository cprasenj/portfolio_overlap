package org.portfolio_overlap.application;

import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Overlap;
import org.portfolio_overlap.domain.model.PortFolio;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;
import org.portfolio_overlap.service.PortfolioOverlapService;

import java.util.*;
import java.util.stream.Collectors;

public class FundOverlap {
    private PortFolio portfolio;
    private PortfolioOverlapService portfolioOverlapService;

    public FundOverlap(List<Fund> funds, PortfolioOverlapService portfolioOverlapService) {
        this.portfolio = new PortFolio(funds);
        this.portfolioOverlapService = portfolioOverlapService;
    }

    public PortFolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(List<Fund> funds) {
        this.portfolio.setFunds(funds);
    }

    public List<Overlap> calculateOverlap(String fundName) throws FundNotFoundException {
        List<Overlap> overlaps = new ArrayList<>();
        Fund givenFund = portfolioOverlapService.getFundByName(fundName);
        for(Fund fund: this.portfolio.getFunds()) {
            overlaps.add(
                    new Overlap(fund, givenFund, portfolioOverlapService.fundOverlap(fund, givenFund))
            );
        }
        return overlaps;
    }

    public Fund addStockToAnExistingFund(String fundName, String stockName) throws FundNotFoundException {
        Stock stock;
        Fund fund = portfolioOverlapService.getFundByName(fundName);
        try {
            stock = portfolioOverlapService.getStockByName(stockName);
        } catch (StockNotFoundException e) {
            stock = portfolioOverlapService.addStock(new Stock(stockName));
        }
        try {
            return portfolioOverlapService.addStockToExistingFund(fund, stock);
        } catch (StockNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshFund() {
        List<Fund> funds = new ArrayList<>();
        for (Fund fund: this.portfolio.getFunds()) {
            try {
                funds.add(this.portfolioOverlapService.getFundByName(fund.getFundName()));
            } catch (FundNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.portfolio.setFunds(funds);
    }
    public List<String> execute(String inputLine) {
        List<String> args = Arrays.asList(inputLine.split(" "));
        String command = args.get(0).trim();
        String head = args.get(0).trim();
        String last = args.get(args.size() - 1).trim();
        List<String> rest = args.subList(1, args.size());
        try {
            if (Objects.equals(command, "CURRENT_PORTFOLIO")) {
                try {
                    List<Fund> funds = new ArrayList<>();
                    for (String fundName: rest) {
                        funds.add(this.portfolioOverlapService.getFundByName(fundName.trim()));
                    }
                    this.setPortfolio(funds);
                } catch (FundNotFoundException e) {
                    return Collections.singletonList("FUND_NOT_FOUND");
                }
                return Collections.emptyList();
            } else if (Objects.equals(command, "CALCULATE_OVERLAP")) {
                try {
                    List<Overlap> overlaps = this.calculateOverlap(String.join(" ", rest).trim());
                    return overlaps.stream().map(Overlap::toString).collect(Collectors.toList());
                } catch (FundNotFoundException e) {
                    return Collections.singletonList("FUND_NOT_FOUND");
                }
            } else if (Objects.equals(last, "OVERLAP_PERCENTAGE%")) {
                try {
                    List<Overlap> overlaps = this.calculateOverlap(head);
                    return overlaps.stream().map(Overlap::toString).collect(Collectors.toList());
                } catch (FundNotFoundException e) {
                    return Collections.singletonList("FUND_NOT_FOUND");
                }
            } else if (Objects.equals(command, "ADD_STOCK")) {
                this.addStockToAnExistingFund(args.get(1), String.join(" ", args.subList(2, args.size())).trim());
                this.refreshFund();
                return Collections.emptyList();
            } else {
                return Collections.singletonList("COMMAND_NOT_FOUND");
            }
        } catch (Exception e) {
            return Collections.singletonList("INVALID_COMMAND");
        }
    }
}
