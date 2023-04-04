package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.portfolio_overlap.dao.FileSystemFundsDao;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSystemDaoTest {
    private FileSystemFundsDao dao;
    @BeforeEach
    void setUp() {
        try {
            dao = new FileSystemFundsDao(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldGetStockIfPresent() throws StockNotFoundException {
        String stockName = "APOLLO HOSPITALS ENTERPRISE LIMITED";
        Stock expected = new Stock(stockName);
        assertEquals(expected, dao.getStock(stockName));
    }

    @Test
    public void shouldThrowErrorIfStockNotPresent() {
        String stockName = "RANDOM";
        StockNotFoundException thrown = Assertions.assertThrows(
                StockNotFoundException.class,
                () -> dao.getStock(stockName), "StockNotFoundException was expected"
        );

        assertEquals("Stock with name RANDOM not present", thrown.getMessage());
    }

    @Test
    public void shouldThrowErrorIfStockNameIsPassedAsNull() {
        StockNotFoundException thrown = Assertions.assertThrows(
                StockNotFoundException.class,
                () -> dao.getStock(null), "StockNotFoundException was expected"
        );

        assertEquals("Stock with name null not present", thrown.getMessage());
    }

    @Test
    public void shouldCreateNewStock() throws StockNotFoundException {
        Stock actual = dao.createStock("new Stock");
        Stock expected = new Stock("NEW STOCK");
        assertEquals(expected, actual);
        assertEquals(expected, dao.getStock("new stock"));
    }

    @Test
    public void shouldCreateNewStockAndTheOperationIsIdempotent() throws StockNotFoundException {
        Stock actual = dao.createStock("new Stock");
        Stock expected = new Stock("NEW STOCK");
        assertEquals(expected, actual);
        assertEquals(expected, dao.getStock("new stock"));
        Stock actual1 = dao.createStock("new Stock");
        assertEquals(expected, actual1);
    }

    @Test
    public void shouldGetFundIfPresent() throws FundNotFoundException {
        String fundName = "UTI_NIFTY_INDEX";
        Fund fund = dao.getFund(fundName);
        assertEquals(fundName, fund.getFundName());
        assertEquals(57, fund.getStocks().size());
    }

    @Test
    public void shouldThrowErrorIfNotPresent() throws FundNotFoundException {
        FundNotFoundException thrown = Assertions.assertThrows(
                FundNotFoundException.class,
                () -> dao.getFund("RANDOM"), "FundNotFoundException was expected"
        );

        assertEquals("Fund with name RANDOM not present", thrown.getMessage());
    }

    @Test
    public void shouldThrowErrorIfNameIsPassedAsNull() throws FundNotFoundException {
        FundNotFoundException thrown = Assertions.assertThrows(
                FundNotFoundException.class,
                () -> dao.getFund(null), "FundNotFoundException was expected"
        );

        assertEquals("Fund with name null not present", thrown.getMessage());
    }

    @Test
    public void shouldUpdateFund() throws FundNotFoundException, StockNotFoundException {
        String fundName = "UTI_NIFTY_INDEX";
        Fund fund = dao.getFund(fundName);
        assertEquals(fundName, fund.getFundName());
        assertEquals(57, fund.getStocks().size());

        Set<String> new_stocks = new HashSet<>();
        new_stocks.add("ADITYA BIRLA FASHION AND RETAIL LIMITED");
        dao.updateFund(fundName, new_stocks);

        Fund updatedFund = dao.getFund(fundName);
        assertEquals(fundName, updatedFund.getFundName());
        assertEquals(58, updatedFund.getStocks().size());
    }

    @Test
    public void shouldThrowErrorWhileUpdatingFundIfFundNotPresent() {
        Set<String> new_stocks = new HashSet<>();
        new_stocks.add("ADITYA BIRLA FASHION AND RETAIL LIMITED");
        FundNotFoundException thrown = Assertions.assertThrows(
                FundNotFoundException.class,
                () -> dao.updateFund("RANDOM", new_stocks), "FundNotFoundException was expected"
        );
        assertEquals("Fund with name RANDOM not present", thrown.getMessage());
    }

    @Test
    public void shouldThrowErrorWhileUpdatingFundIfStockNotPresent() {
        Set<String> new_stocks = new HashSet<>();
        new_stocks.add("RANDOM");
        StockNotFoundException thrown = Assertions.assertThrows(
                StockNotFoundException.class,
                () -> dao.updateFund("UTI_NIFTY_INDEX", new_stocks), "StockNotFoundException was expected"
        );
        assertEquals("Stock with name RANDOM not present", thrown.getMessage());
    }
}
