package com.kiegame.mobile.repository.entity.receive;

/**
 * Created by: var_rain.
 * Created date: 2020/9/21.
 * Description: 菜单权限
 */
public class MenuEntry {

    /**
     * menuId : 3911548442655744
     * menuName : APP-网费充值
     * menuUrl : app-recharge
     * menuIcon : null
     * isDisplay : null
     * isLeaf : 1
     * parentId : 3911547427929088
     * project : app
     * sort : null
     * lastUpdateTime : null
     * createDate : null
     * isDelete : null
     * deleteDate : null
     */

    private String menuId;
    private String menuName;
    private String menuUrl;
    private Object menuIcon;
    private Object isDisplay;
    private int isLeaf;
    private String parentId;
    private String project;
    private Object sort;
    private Object lastUpdateTime;
    private Object createDate;
    private Object isDelete;
    private Object deleteDate;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Object getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(Object menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Object getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Object isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
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
}
