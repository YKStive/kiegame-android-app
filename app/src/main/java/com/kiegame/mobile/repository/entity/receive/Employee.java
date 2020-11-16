package com.kiegame.mobile.repository.entity.receive;

public class Employee {

    /**
     * empId :
     * shopManager : true
     * empName :
     * roleName :
     */
    private String empId;
    private boolean shopManager;
    private String empName;
    private String roleName;

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public void setShopManager(boolean shopManager) {
        this.shopManager = shopManager;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmpId() {
        return empId;
    }

    public boolean isShopManager() {
        return shopManager;
    }

    public String getEmpName() {
        return empName;
    }

    public String getRoleName() {
        return roleName;
    }
}
