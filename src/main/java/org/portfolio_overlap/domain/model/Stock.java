package org.portfolio_overlap.domain.model;

import java.util.Objects;

public class Stock {
    public String getName() {
        return name;
    }

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(name, stock.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Stock(String name) {
        this.name = name;
    }

    public Stock(Stock that) {
        this(that.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
