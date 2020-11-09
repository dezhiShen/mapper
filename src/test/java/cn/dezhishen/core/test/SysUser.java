package cn.dezhishen.core.test;

import cn.dezhishen.core.annotations.DataPermission;
import cn.dezhishen.core.annotations.ParamType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shendehi
 */
@Setter
@Getter
public class SysUser {
    @DataPermission(field = "userIds", paramType = ParamType.List)
    private String userId;
}
