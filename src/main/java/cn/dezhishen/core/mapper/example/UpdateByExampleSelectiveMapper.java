package cn.dezhishen.core.mapper.example;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface UpdateByExampleSelectiveMapper<T> {
    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @param record
     * @param example
     * @return
     */
    @UpdateProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int updateByExampleSelective(@Param("record") T record, @Param("example") Object example);
}
