package com.zrt.zenb.entity;

import java.util.List;
import java.util.Map;

import com.zrt.zenb.entity.PMsgSjInfo;

public interface PMsgSjInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    int insert(PMsgSjInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    int insertSelective(PMsgSjInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    PMsgSjInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(PMsgSjInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sjinfo
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(PMsgSjInfo record);
    
    List<PMsgSjInfo> selectSjInfoRecord(Map<String, Object> map);
    
    PMsgSjInfo selectImsiByMsisdn(String msisdnLikeKey);
}
