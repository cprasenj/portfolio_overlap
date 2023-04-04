package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.portfolio_overlap.dao.FileSystemFundsDao;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.exception.StockNotFoundException;
import org.portfolio_overlap.service.PortfolioOverlapService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioOverlapServiceTest {
    private PortfolioOverlapService service;

    @BeforeEach
    void setUp() {
        try {
            service = new PortfolioOverlapService(new FileSystemFundsDao(null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMiraAssetAndAxisBlueChip() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP"),
                service.getFundByName("AXIS_BLUECHIP")
        );
        assertEquals(39.130435943603516, actual);
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMiraAssetAndICICIBlueChip() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP"),
                service.getFundByName("ICICI_PRU_BLUECHIP")
        );
        assertEquals(38.095237731933594, actual);
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMiraAssetAndUTINiftyIndex() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP"),
                service.getFundByName("UTI_NIFTY_INDEX")
        );
        assertEquals(65.51724243164062, actual);
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMarketAssetLargeCapAndAxisBlueChip() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_LARGE_CAP"),
                service.getFundByName("AXIS_BLUECHIP")
        );
        assertEquals(43.75, actual);
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMarketAssetLargeCapAndICICIBlueChip() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_LARGE_CAP"),
                service.getFundByName("ICICI_PRU_BLUECHIP")
        );
        assertEquals(44.61538314819336, actual);
    }

    @Test
    public void shouldCalculatePortfolioOverlapBetweenMarketAssetLargeCapAndUTINiftyIndex() throws FundNotFoundException {
        float actual = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_LARGE_CAP"),
                service.getFundByName("UTI_nIFTY_INDEX")
        );
        assertEquals(95.0, actual);
    }

    @Test
    public void shouldAddNewStockToExistingFund() throws FundNotFoundException, StockNotFoundException {
        Fund fundBeforeUpdate = service.getFundByName("AXIS_BLUECHIP");

        float actualBeforeUpdate = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP"),
                fundBeforeUpdate
        );
        assertEquals(39.130435943603516, actualBeforeUpdate);

        service.addStock(new Stock("TCS"));

        service.addStockToExistingFund(
                fundBeforeUpdate,
                service.getStockByName("TCS")
        );
        Fund fundAfterUpdate = service.getFundByName("AXIS_BLUECHIP");
        assertEquals(33, fundBeforeUpdate.getStocks().size());
        assertEquals(34, fundAfterUpdate.getStocks().size());
        List<Stock> stockIntersection = new ArrayList<>(fundAfterUpdate.stocksDoesNotBelongsTo(fundBeforeUpdate));
        assertEquals(1, stockIntersection.size());
        assertEquals(
                "TCS",
                stockIntersection.get(0).getName()
        );

        float actualAfterUpdate = service.fundOverlap(
                service.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP"),
                service.getFundByName("AXIS_BLUECHIP")
        );
        assertEquals(38.70967483520508, actualAfterUpdate);
    }

    @Test
    public void shouldAddExistingStockToExistingFund() throws FundNotFoundException, StockNotFoundException {
        Fund fundBeforeUpdate = service.getFundByName("AXIS_BLUECHIP");
        service.addStockToExistingFund(
                fundBeforeUpdate,
                service.getStockByName("RATNAMANI METALS & TUBES LIMITED")
        );
        Fund fundAfterUpdate = service.getFundByName("AXIS_BLUECHIP");
        assertEquals(33, fundBeforeUpdate.getStocks().size());
        assertEquals(34, fundAfterUpdate.getStocks().size());
        List<Stock> stockIntersection = new ArrayList<>(fundAfterUpdate.stocksDoesNotBelongsTo(fundBeforeUpdate));
        assertEquals(1, stockIntersection.size());
        assertEquals(
                "RATNAMANI METALS & TUBES LIMITED",
                stockIntersection.get(0).getName()
        );
    }
}
