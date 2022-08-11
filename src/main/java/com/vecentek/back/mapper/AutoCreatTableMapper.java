package com.vecentek.back.mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 * @author liujz
 * @date 2022/8/8
 * @apiNote
 */
@Repository
public interface AutoCreatTableMapper {
    /**
     * 建表
     * @param createTabelSql
     */
    @Update("${createTabelSql}")
    void createTable(@Param("createTabelSql")String createTabelSql);
}
