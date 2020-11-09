package cn.dezhishen.core.mapper.example;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface ExampleMapper<T> extends
        SelectByExampleMapper<T>,
        SelectOneByExampleMapper<T>,
        SelectCountByExampleMapper<T>,
        DeleteByExampleMapper<T>,
        UpdateByExampleMapper<T>,
        UpdateByExampleSelectiveMapper<T> {

}
