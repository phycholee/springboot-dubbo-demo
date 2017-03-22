package com.llf.springboot.dubbo.mapper;

import com.llf.springboot.dubbo.model.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by PhychoLee on 2017/3/21 15:53.
 */
public interface UserMapper {

    @Select("select id,name,age,introduce from user where name = #{name}")
//    @Results({
//            @Result(id=true,property="id",column="id"),
//            @Result(property="name",column="name"),
//            @Result(property="age",column="age"),
//            @Result(property="introduce",column="introduce")
//    })
    User getUserByName(String name);
}
