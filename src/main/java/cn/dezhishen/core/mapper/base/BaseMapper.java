package cn.dezhishen.core.mapper.base;

import cn.dezhishen.core.mapper.base.delete.BaseDeleteMapper;
import cn.dezhishen.core.mapper.base.select.BaseSelectMapper;
import cn.dezhishen.core.mapper.base.update.BaseUpdateMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;

/**
 * @param <T>
 * @author shendezhi
 */
@RegisterMapper
public interface BaseMapper<T> extends BaseUpdateMapper<T>, BaseInsertMapper<T>, BaseSelectMapper<T>, BaseDeleteMapper<T> {
}
