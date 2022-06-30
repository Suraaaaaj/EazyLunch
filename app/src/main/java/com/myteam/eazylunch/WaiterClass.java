package com.myteam.eazylunch;

public class WaiterClass {
    private String waiterName;
    private String waiterNo;
    private String waiterID;

    public WaiterClass(){

    }

    public WaiterClass(String waiterID, String waiterName,String waiterNo) {
        this.waiterID = waiterID;
        this.waiterName = waiterName;
        this.waiterNo =waiterNo;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getWaiterNo() {
        return waiterNo;
    }

    public void setWaiterNo(String waiterNo) {
        this.waiterNo = waiterNo;
    }

    public String getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(String waiterID) {
        this.waiterID = waiterID;
    }
}
