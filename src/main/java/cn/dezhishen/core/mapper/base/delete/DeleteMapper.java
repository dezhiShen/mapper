package cn.dezhishen.core.mapper.base.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface DeleteMapper<T> {
    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
    @DeleteProvider(type = BaseDeleteProvider.class, method = "dynamicSQL")
    int delete(T record);
}
