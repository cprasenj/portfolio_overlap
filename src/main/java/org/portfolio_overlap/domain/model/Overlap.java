package org.portfolio_overlap.domain.model;

import java.util.Objects;

public class Overlap {
    private Fund one;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Overlap overlap = (Overlap) o;
        return Float.compare(overlap.overlapPercentage, overlapPercentage) == 0 && Objects.equals(one, overlap.one) && Objects.equals(another, overlap.another);
    }

    @Override
    public int hashCode() {
        return Objects.hash(one, another, overlapPercentage);
    }

    private Fund another;
    private float overlapPercentage;
    public Overlap(Fund one, Fund another, float overlapPercentage) {
        this.one = one;
        this.another = another;
        this.overlapPercentage = overlapPercentage;
    }

    @Override
    public String toString() {
        return another + " " + one + " " + String.format("%.02f", overlapPercentage) + "%";
    }
}
