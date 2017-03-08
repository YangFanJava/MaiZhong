package com.maizhong.mapper;

import com.maizhong.pojo.TbAdvertPublish;
import com.maizhong.pojo.TbAdvertPublishExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbAdvertPublishMapper {
    long countByExample(TbAdvertPublishExample example);

    int deleteByExample(TbAdvertPublishExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbAdvertPublish record);

    int insertSelective(TbAdvertPublish record);

    List<TbAdvertPublish> selectByExample(TbAdvertPublishExample example);

    TbAdvertPublish selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbAdvertPublish record, @Param("example") TbAdvertPublishExample example);

    int updateByExample(@Param("record") TbAdvertPublish record, @Param("example") TbAdvertPublishExample example);

    int updateByPrimaryKeySelective(TbAdvertPublish record);

    int updateByPrimaryKey(TbAdvertPublish record);
}