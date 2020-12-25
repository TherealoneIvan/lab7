package bmstu;

public class StorageData {
    private Integer startSeq;
    private Integer endSeq;
    private long timeLife;

    public StorageData(Integer startSeq, Integer endSeq, long timeLife) {
        this.startSeq = startSeq;
        this.endSeq = endSeq;
        this.timeLife = timeLife;
    }

    public Integer getStartSeq() {
        return startSeq;
    }

    public void setStartSeq(Integer startSeq) {
        this.startSeq = startSeq;
    }

    public Integer getEndSeq() {
        return endSeq;
    }

    public void setEndSeq(Integer endSeq) {
        this.endSeq = endSeq;
    }

    public long getTimeLife() {
        return timeLife;
    }

    public void setTimeLife(long timeLife) {
        this.timeLife = timeLife;
    }
}
