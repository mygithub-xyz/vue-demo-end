package com.nanjing.entity;


import java.math.BigDecimal;

public class CountOrder {
    private String coName;
    private String createTime;
    private String endTime;
    private String acctfileno;
    private String countAmt;
    private BigDecimal countMoney;
    private String  date;
    private String maker;
    private String checker;
    private Object program;
    /*{       "":"李白",
            "createTime":"2020-9-30",
            "acctfileno":"0000000213124832",
            "countAmt":"壹佰元整",
            "countMoney":"100.00",
            "date":"2020-9-18",
            "maker":"猪八戒",
            "checker":"孙悟空",
            "program":[{"pro":"挂号费","amt":"11.00"},
        {"pro":"住院费","amt":"12.00"},
        {"pro":"点滴费","amt":"13.00"}
                ]
    }*/

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getAcctfileno() {
        return acctfileno;
    }

    public void setAcctfileno(String acctfileno) {
        this.acctfileno = acctfileno;
    }

    public String getCountAmt() {
        return countAmt;
    }

    public void setCountAmt(String countAmt) {
        this.countAmt = countAmt;
    }

    public BigDecimal getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(BigDecimal countMoney) {
        this.countMoney = countMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Object getProgram() {
        return program;
    }

    public void setProgram(Object program) {
        this.program = program;
    }
}
