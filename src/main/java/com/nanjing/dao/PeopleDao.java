package com.nanjing.dao;

import com.nanjing.pojo.People;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PeopleDao {
    @Insert("insert into people(pname,countryid,createtime) values(#{pname},#{countryid},#{createtime})")
    public void save(People people);

    @Update("update people set pname=#{pname},countryid=#{countryid},createtime=#{createtime} where pid=#{pid}")
    public void update(People people);

    @Delete("delete from people where pid=#{pid}")
    public void delete(Long pid);

    @Select("select * from people")
    public List<People> getAll();

    @Select("select * from people where pid=#{pid}")
    public People findOne(Long pid);

    public List<People> getPeopleList(@Param("pname") String pname, @Param("countryid") String countryid);
    public List<People> queryCustomerByIds(@Param("ids") Long [] ids );
    public People userLogin(@Param("pname") String pname, @Param("countryid") String countryid);
}