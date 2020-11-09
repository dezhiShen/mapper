package cn.dezhishen.core.mapper.base.delete;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BaseDeleteMapper<T> extends DeleteMapper<T>, DeleteByPrimaryKeyMapper<T> {
}
