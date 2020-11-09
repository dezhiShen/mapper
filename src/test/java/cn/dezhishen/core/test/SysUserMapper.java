package cn.dezhishen.core.test;

import cn.dezhishen.core.mapper.Mapper;

/**
 * @author shendezhi
 */
public interface SysUserMapper extends Mapper<SysUser> {
    SysUser selectById(String userId);

    SysUser selectByIdForIgnored(String userId);

}
