package cn.dezhishen.core.mapper.example;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface SelectOneByExampleMapper<T> {
    /**
     * 根据Example条件进行查询
     *
     * @param example
     * @return
     */
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    T selectOneByExample(Object example);
}
