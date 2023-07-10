package org.watermelon;

public class Like {

    private Integer idSource;

    private Integer idTarget;

    public Like(Integer idSource, Integer idTarget) {
        this.idSource = idSource;
        this.idTarget = idTarget;
    }

    public Integer getIdSource() {
        return idSource;
    }

    public void setIdSource(Integer idSource) {
        this.idSource = idSource;
    }

    public Integer getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(Integer idTarget) {
        this.idTarget = idTarget;
    }
}
