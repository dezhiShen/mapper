package cn.dezhishen.core.mapper.batch;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 通用Mapper接口,批量插入
 *
 * @param <T> 不能为空
 * @author shendezhi
 */
@RegisterMapper
public interface BatchInsertMapper<T> {

    /**
     * 批量插入,null直接插入null
     *
     * @param list
     * @return
     */
    @SelectProvider(type = BaseBatchInsertProvider.class, method = "dynamicSQL")
    int batchInsert(@Param("list") List<T> list);

}
