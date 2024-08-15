package graph;

import java.io.Serializable;
import java.util.List;

public class Weight implements Serializable {

    private int numTimesRecordedInLogs;
    private List<Short> positionsInLogs;
    private byte simpleProb;

    public Weight(byte simpleProb) {
        this.simpleProb = simpleProb;
    }

    public byte getWeight() {
        return simpleProb;
    }

    public void setWeight(byte simpleProb) {
        this.simpleProb = simpleProb;
    }

    @Override
    public String toString() {
        return String.valueOf(simpleProb);
    }

}
