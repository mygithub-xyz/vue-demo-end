package com.nanjing.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * 总条数 total
 * 当前页的行列表 rows
 */
@ApiModel
public class PageResult implements Serializable {
    @ApiModelProperty(value = "分页查询总条数")
    private long total;//总条数
    @ApiModelProperty(value = "分页结果集")
    private List rows;

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}