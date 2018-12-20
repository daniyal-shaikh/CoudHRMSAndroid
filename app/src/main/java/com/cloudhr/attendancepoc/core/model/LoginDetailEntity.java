package com.cloudhr.attendancepoc.core.model;

/**
 * Created by daniyalshaikh on 17/12/18.
 */

public class LoginDetailEntity {


    /**
     * employeeid : 1
     * employeename : Daniyal
     * employeecode : CHR1
     * phoneno1 : 9702943935
     * phoneno2 : 8355992808
     * emailid : shaikhdani26@gmail.com
     * branchid : 1
     * branchname : Clour HR - Mumbai
     * branchlocation : MUmbai
     * companyid : 1
     * companyname : Cloud HR Technology Pvt. Ltd
     * SaveStatus : 0
     */

    private int employeeid;
    private String employeename;
    private String employeecode;
    private String phoneno1;
    private String phoneno2;
    private String emailid;
    private int branchid;
    private String branchname;
    private String branchlocation;
    private int companyid;
    private String companyname;
    private String lattitude;
    private String longitude;
    private String SaveStatus;

    public int getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(int employeeid) {
        this.employeeid = employeeid;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getPhoneno1() {
        return phoneno1;
    }

    public void setPhoneno1(String phoneno1) {
        this.phoneno1 = phoneno1;
    }

    public String getPhoneno2() {
        return phoneno2;
    }

    public void setPhoneno2(String phoneno2) {
        this.phoneno2 = phoneno2;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchlocation() {
        return branchlocation;
    }

    public void setBranchlocation(String branchlocation) {
        this.branchlocation = branchlocation;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }


    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSaveStatus() {
        return SaveStatus;
    }

    public void setSaveStatus(String SaveStatus) {
        this.SaveStatus = SaveStatus;
    }
}
