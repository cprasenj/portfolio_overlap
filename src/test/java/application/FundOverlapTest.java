package application;

import org.junit.jupiter.api.Test;
import org.portfolio_overlap.application.FundOverlap;
import org.portfolio_overlap.dao.FileSystemFundsDao;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Overlap;
import org.portfolio_overlap.domain.model.PortFolio;
import org.portfolio_overlap.exception.FundNotFoundException;
import org.portfolio_overlap.service.PortfolioOverlapService;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FundOverlapTest {
    @Test
    public void shouldSetPortfolio() throws IOException, FundNotFoundException {
        PortfolioOverlapService portfolioOverlapService = new PortfolioOverlapService(new FileSystemFundsDao(null));
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), portfolioOverlapService);
        List<Fund> funds = Arrays.asList(
                portfolioOverlapService.getFundByName("AXIS_BLUECHIP"),
                portfolioOverlapService.getFundByName("ICICI_PRU_BLUECHIP"),
                portfolioOverlapService.getFundByName("UTI_NIFTY_INDEX")
        );
        fundOverlap.setPortfolio(funds);
        assertEquals(new PortFolio(funds), fundOverlap.getPortfolio());
    }

    @Test
    public void shouldGetPortfolioOverlap() throws IOException, FundNotFoundException {
        PortfolioOverlapService portfolioOverlapService = new PortfolioOverlapService(new FileSystemFundsDao(null));
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), portfolioOverlapService);
        Fund axisBlueChip = portfolioOverlapService.getFundByName("AXIS_BLUECHIP");
        Fund iciciPruBlueChip = portfolioOverlapService.getFundByName("ICICI_PRU_BLUECHIP");
        Fund utiNiftyIndex = portfolioOverlapService.getFundByName("UTI_NIFTY_INDEX");
        Fund miraeAssetEmergingBluechip = portfolioOverlapService.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP");

        List<Fund> funds = Arrays.asList(axisBlueChip, iciciPruBlueChip, utiNiftyIndex);

        fundOverlap.setPortfolio(funds);
        List<Overlap> expected = Arrays.asList(
                new Overlap(axisBlueChip, miraeAssetEmergingBluechip, 39.130436F),
                new Overlap(iciciPruBlueChip, miraeAssetEmergingBluechip, 38.095238F),
                new Overlap(utiNiftyIndex, miraeAssetEmergingBluechip, 65.51724F)

        );
        List<Overlap> actual = fundOverlap.calculateOverlap("MIRAE_ASSET_EMERGING_BLUECHIP");
        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetPortfolioOverlapAfterAddingAStock() throws IOException, FundNotFoundException {
        PortfolioOverlapService portfolioOverlapService = new PortfolioOverlapService(new FileSystemFundsDao(null));
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), portfolioOverlapService);
        Fund axisBlueChip = portfolioOverlapService.getFundByName("AXIS_BLUECHIP");
        Fund iciciPruBlueChip = portfolioOverlapService.getFundByName("ICICI_PRU_BLUECHIP");
        Fund utiNiftyIndex = portfolioOverlapService.getFundByName("UTI_NIFTY_INDEX");
        Fund miraeAssetEmergingBluechip = portfolioOverlapService.getFundByName("MIRAE_ASSET_EMERGING_BLUECHIP");

        List<Fund> funds = Arrays.asList(axisBlueChip, iciciPruBlueChip, utiNiftyIndex);
        fundOverlap.addStockToAnExistingFund("AXIS_BLUECHIP", "TCS");
        fundOverlap.setPortfolio(funds);
        List<Overlap> expected = Arrays.asList(
                new Overlap(axisBlueChip, miraeAssetEmergingBluechip, 39.130436F),
                new Overlap(iciciPruBlueChip, miraeAssetEmergingBluechip, 38.095238F),
                new Overlap(utiNiftyIndex, miraeAssetEmergingBluechip, 65.51724F)

        );
        List<Overlap> actual = fundOverlap.calculateOverlap("MIRAE_ASSET_EMERGING_BLUECHIP");
        assertEquals(expected, actual);
    }

    @Test
    public void shouldExecuteGivenSetOfCommandsScenario1() throws IOException {
        PortfolioOverlapService portfolioOverlapService = new PortfolioOverlapService(new FileSystemFundsDao(null));
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), portfolioOverlapService);
        assertEquals(
                new ArrayList<>(),
                fundOverlap.execute("CURRENT_PORTFOLIO AXIS_BLUECHIP ICICI_PRU_BLUECHIP UTI_NIFTY_INDEX")
        );
        assertEquals(
                Arrays.asList(
                        "MIRAE_ASSET_EMERGING_BLUECHIP AXIS_BLUECHIP 39.13%",
                        "MIRAE_ASSET_EMERGING_BLUECHIP ICICI_PRU_BLUECHIP 38.10%",
                        "MIRAE_ASSET_EMERGING_BLUECHIP UTI_NIFTY_INDEX 65.52%"
                ),
                fundOverlap.execute("CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP")
        );
        assertEquals(
                Arrays.asList(
                        "MIRAE_ASSET_LARGE_CAP AXIS_BLUECHIP 43.75%",
                        "MIRAE_ASSET_LARGE_CAP ICICI_PRU_BLUECHIP 44.62%",
                        "MIRAE_ASSET_LARGE_CAP UTI_NIFTY_INDEX 95.00%"
                ),
                fundOverlap.execute("CALCULATE_OVERLAP MIRAE_ASSET_LARGE_CAP")
        );
        assertEquals(
                new ArrayList<>(),
                fundOverlap.execute("ADD_STOCK AXIS_BLUECHIP TCS")
        );
        assertEquals(
                Arrays.asList(
                        "MIRAE_ASSET_EMERGING_BLUECHIP AXIS_BLUECHIP 38.71%",
                        "MIRAE_ASSET_EMERGING_BLUECHIP ICICI_PRU_BLUECHIP 38.10%",
                        "MIRAE_ASSET_EMERGING_BLUECHIP UTI_NIFTY_INDEX 65.52%"
                ),
                fundOverlap.execute("CALCULATE_OVERLAP MIRAE_ASSET_EMERGING_BLUECHIP")
        );
    }

    @Test
    public void shouldExecuteGivenSetOfCommandsScenario2() throws IOException {
        PortfolioOverlapService portfolioOverlapService = new PortfolioOverlapService(new FileSystemFundsDao(null));
        FundOverlap fundOverlap = new FundOverlap(new ArrayList<>(), portfolioOverlapService);
        assertEquals(
                new ArrayList<>(),
                fundOverlap.execute("CURRENT_PORTFOLIO UTI_NIFTY_INDEX AXIS_MIDCAP PARAG_PARIKH_FLEXI_CAP")
        );
        assertEquals(
                Arrays.asList(
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX UTI_NIFTY_INDEX 20.37%",
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX AXIS_MIDCAP 14.81%",
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX PARAG_PARIKH_FLEXI_CAP 7.41%"
                ),
                fundOverlap.execute("CALCULATE_OVERLAP ICICI_PRU_NIFTY_NEXT_50_INDEX")
        );
        assertEquals(
                Collections.singletonList("FUND_NOT_FOUND"),
                fundOverlap.execute("CALCULATE_OVERLAP NIPPON_INDIA_PHARMA_FUND")
        );
        assertEquals(
                Collections.emptyList(),
                fundOverlap.execute("ADD_STOCK PARAG_PARIKH_FLEXI_CAP NOCIL")
        );
        assertEquals(
                Collections.emptyList(),
                fundOverlap.execute("ADD_STOCK AXIS_MIDCAP NOCIL")
        );
        assertEquals(
                Arrays.asList(
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX UTI_NIFTY_INDEX 20.37%",
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX AXIS_MIDCAP 14.68%",
                        "ICICI_PRU_NIFTY_NEXT_50_INDEX PARAG_PARIKH_FLEXI_CAP 7.32%"
                ),
                fundOverlap.execute("CALCULATE_OVERLAP ICICI_PRU_NIFTY_NEXT_50_INDEX")
        );

    }
}
