package cn.dezhishen.core.mapper.base.update;

import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author shendezhi
 */
@RegisterMapper
public interface BaseUpdateMapper<T> {
    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    @UpdateProvider(type = BaseUpdateProvider.class, method = "dynamicSQL")
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    @UpdateProvider(type = BaseUpdateProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelective(T record);
}
