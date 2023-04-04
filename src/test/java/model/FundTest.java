package model;

import org.junit.jupiter.api.Test;
import org.portfolio_overlap.domain.model.Fund;
import org.portfolio_overlap.domain.model.Stock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FundTest {
    @Test
    public void shouldBeEqualIfFundNamesAreSameWithSameStocks() {
        Fund one = new Fund("a fund", new HashSet<>());
        Fund another = new Fund("a fund", new HashSet<>());
        assertEquals(one, another);
    }

    @Test
    public void shouldNotBeEqualIfFundNamesAreDifferentWithSameStocks() {
        Fund one = new Fund("a fund", new HashSet<>());
        Fund another = new Fund("another fund", new HashSet<>());
        assertNotEquals(one, another);
    }

    @Test
    public void shouldNotBeEqualIfFundNamesAreSameWithDifferentStocks() {
        Fund one = new Fund("a fund", new HashSet<>());
        Fund another = new Fund("a fund", new HashSet<>(Collections.singleton(new Stock("a stock"))));
        assertNotEquals(one, another);
    }

    @Test
    public void shouldGetIntersectionOfTwoFund() {
        Fund one = new Fund(
                "a fund",
                new HashSet<>(Arrays.asList(new Stock("stock1"), new Stock("stock2")))
        );
        Fund another = new Fund(
                "a fund",
                new HashSet<>(Arrays.asList(new Stock("stock1"), new Stock("stock3")))
        );
        Set<Stock> actual = one.intersection(another);
        assertEquals(new HashSet<>(Collections.singleton(new Stock("stock1"))), actual);
    }

    @Test
    public void shouldGetStocksDoesNotBelongsToOtherFund() {
        Fund one = new Fund(
                "a fund",
                new HashSet<>(Arrays.asList(new Stock("stock1"), new Stock("stock2")))
        );
        Fund another = new Fund(
                "a fund",
                new HashSet<>(Arrays.asList(new Stock("stock1"), new Stock("stock3")))
        );
        Set<Stock> actual = one.stocksDoesNotBelongsTo(another);
        assertEquals(new HashSet<>(Collections.singleton(new Stock("stock2"))), actual);
    }
}
