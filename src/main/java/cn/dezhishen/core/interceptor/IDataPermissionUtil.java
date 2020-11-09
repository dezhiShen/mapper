package cn.dezhishen.core.interceptor;


import java.util.Map;

/**
 * @author shendzhi
 */
public interface IDataPermissionUtil {
    /**
     * 获取当前sql的权限要求
     *
     * @return
     */
    Map<String, Object> getAll();

    /**
     * 是否跳过权限过滤
     *
     * @return
     */
    Boolean isSkip();
}
