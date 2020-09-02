package com.nanjing.service;

import com.nanjing.entity.PageResult;
import com.nanjing.pojo.People;

import java.util.List;

public interface PeopleService {
    //用户登陆接口
   public People userLogin(String pname,String countryid);
    //获取全部用户数据
    public List<People> getUserList();

    /**
     * 分页查询
     */
    public PageResult findPage(People people, int pageNo, int pageSize);
    /* */

    /**
     * 根据条件分页查询
     *//*
    public PageResult findPage(TbBrand brand, int pageNo, int pageSize);*/
    //新增用户数据
    public void createUser(People people);

    //获取指定id用户信息
    public People getUser(Long pid);

    //更新指定id用户信息
    public void updateUser(People people);

    //删除指定id用户
    public void deleteUser(Long pid);
    //
    public List<People> queryCustomerByIds(Long [] ids);
}
