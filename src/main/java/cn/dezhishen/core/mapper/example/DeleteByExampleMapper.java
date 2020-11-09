package cn.dezhishen.core.mapper.example;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface DeleteByExampleMapper<T> {
    /**
     * 根据Example条件删除数据
     *
     * @param example
     * @return
     */
    @DeleteProvider(type = ExampleProvider.class, method = "dynamicSQL")
    int deleteByExample(Object example);
}
