package cn.dezhishen.core.mapper.base.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface DeleteByPrimaryKeyMapper<T> {
    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key
     * @return
     */
    @DeleteProvider(type = BaseDeleteProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKey(T key);
}
