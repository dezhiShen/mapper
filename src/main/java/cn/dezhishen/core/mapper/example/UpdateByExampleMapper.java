package cn.dezhishen.core.mapper.example;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface UpdateByExampleMapper<T> {
    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     *
     * @param record
     * @param example
     * @return
     */
    @UpdateProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int updateByExample(@Param("record") T record, @Param("example") Object example);
}
