package cn.dezhishen.core.mapper;

import cn.dezhishen.core.mapper.base.BaseMapper;
import cn.dezhishen.core.mapper.batch.BatchInsertMapper;
import cn.dezhishen.core.mapper.example.ExampleMapper;
import cn.dezhishen.core.mapper.exists.ExistsMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Marker;

/**
 * @param <T> 泛型
 * @author shendezhi
 */
@RegisterMapper
public interface Mapper<T>
        extends
        BaseMapper<T>,
        BatchInsertMapper<T>,
        ExistsMapper<T>,
        ExampleMapper<T>,
        Marker {
}
