package com.nanjing.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nanjing.dao.PeopleDao;
import com.nanjing.entity.PageResult;
import com.nanjing.pojo.People;
import com.nanjing.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private PeopleDao peopleDao;

    @Override
    public People userLogin(String pname,String countryid) {
        return peopleDao.userLogin(pname, countryid);
    }

    @Override
    public List<People> getUserList() {
        return peopleDao.getAll();
    }

    /**
     * 要使用分页的对象
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(People people, int pageNo, int pageSize) {

        PageHelper.startPage(pageNo, pageSize);
        Page<People> page = (Page<People>) peopleDao.getPeopleList(people.getPname(), people.getCountryid());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void createUser(People people) {
        peopleDao.save(people);
    }

    @Override
    public People getUser(Long pid) {
        return peopleDao.findOne(pid);
    }

    @Override
    public void updateUser(People people) {
        people.setPid(Math.toIntExact(people.getPid()));
        peopleDao.update(people);
    }

    @Override
    public void deleteUser(Long pid) {
        peopleDao.delete(pid);
    }

    @Override
    public List<People> queryCustomerByIds(Long[] ids) {
        return peopleDao.queryCustomerByIds(ids);
    }

}
