package com.kaisheng.tms.mapper;

import com.kaisheng.tms.entity.AccountRolesExample;
import com.kaisheng.tms.entity.AccountRolesKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountRolesMapper {
    long countByExample(AccountRolesExample example);

    int deleteByExample(AccountRolesExample example);

    int deleteByPrimaryKey(AccountRolesKey key);

    int insert(AccountRolesKey record);

    int insertSelective(AccountRolesKey record);

    List<AccountRolesKey> selectByExample(AccountRolesExample example);

    int updateByExampleSelective(@Param("record") AccountRolesKey record, @Param("example") AccountRolesExample example);

    int updateByExample(@Param("record") AccountRolesKey record, @Param("example") AccountRolesExample example);
}