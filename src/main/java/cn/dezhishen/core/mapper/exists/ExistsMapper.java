package cn.dezhishen.core.mapper.exists;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,查询
 *
 * @param <T> 不能为空
 * @author shendezhi
 */
@RegisterMapper
public interface ExistsMapper<T> {
    /**
     * 根据字段查询是否存在
     *
     * @param t
     * @return
     */
    @SelectProvider(type = BaseExistsProvider.class, method = "dynamicSQL")
    boolean exists(T t);
}
