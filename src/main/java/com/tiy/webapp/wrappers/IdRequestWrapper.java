package com.tiy.webapp.wrappers;

/**
 * Created by Paul Dennis on 2/5/2017.
 */
public class IdRequestWrapper {

    private Integer systemId;
    private Integer shipId;
    private Integer planetId;
    private Integer tunnelId;
    private Integer techId;
    private Integer raceId;

    private Integer productionNumber;

    public IdRequestWrapper () {

    }


    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public Integer getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Integer planetId) {
        this.planetId = planetId;
    }

    public Integer getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(Integer tunnelId) {
        this.tunnelId = tunnelId;
    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }

    public Integer getProductionNumber() {
        return productionNumber;
    }

    public void setProductionNumber(Integer productionNumber) {
        this.productionNumber = productionNumber;
    }
}
