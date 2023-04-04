package org.portfolio_overlap.domain.model;

import java.util.List;
import java.util.Objects;

public class PortFolio {
    private List<Fund> funds;

    public List<Fund> getFunds() {
        return funds;
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public PortFolio(List<Fund> funds) {
        this.funds = funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortFolio portFolio = (PortFolio) o;
        return Objects.equals(funds, portFolio.funds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funds);
    }
}
