package com.kiegame.mobile.repository.entity.receive;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description: 用户登录返回数据对象
 */
public class LoginEntity {

    /**
     * empId : 80803503964748839937
     * loginCode : test
     * loginPass : null
     * empState : 1
     * empName : 李四
     * serviceId : 80803499721711205376
     * serviceName : 奥克斯广场店铺3
     * roleName : 吧台
     * relationService : 奥克斯广场
     * gender : 1
     * headImg : http://10.168.1.200:8080/img/111.png
     * idCard : null
     * telephone : 18200577868
     * operatorId : null
     * operatorName : null
     * operateTime : null
     * lastUpdateTime : null
     * createDate : null
     * isDelete : null
     * deleteDate : null
     * loginToken : 1A4633EB507BD1EA3F3239A410D5A3D0AC984097BA258C3D8602FB9ED2B526F9
     */

    private String empId;
    private String loginCode;
    private Object loginPass;
    private int empState;
    private String empName;
    private String serviceId;
    private String serviceName;
    private String roleName;
    private String relationService;
    private int gender;
    private String headImg;
    private Object idCard;
    private String telephone;
    private Object operatorId;
    private Object operatorName;
    private Object operateTime;
    private Object lastUpdateTime;
    private Object createDate;
    private Object isDelete;
    private Object deleteDate;
    private String loginToken;
    private List<MenuEntry> menuList;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Object getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(Object loginPass) {
        this.loginPass = loginPass;
    }

    public int getEmpState() {
        return empState;
    }

    public void setEmpState(int empState) {
        this.empState = empState;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRelationService() {
        return relationService;
    }

    public void setRelationService(String relationService) {
        this.relationService = relationService;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Object getIdCard() {
        return idCard;
    }

    public void setIdCard(Object idCard) {
        this.idCard = idCard;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Object getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Object operatorId) {
        this.operatorId = operatorId;
    }

    public Object getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(Object operatorName) {
        this.operatorName = operatorName;
    }

    public Object getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Object operateTime) {
        this.operateTime = operateTime;
    }

    public Object getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Object lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Object isDelete) {
        this.isDelete = isDelete;
    }

    public Object getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Object deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public List<MenuEntry> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuEntry> menuList) {
        this.menuList = menuList;
    }
}
