package cn.dezhishen.core.test;

import cn.dezhishen.core.interceptor.IDataPermissionUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shendezhi
 */
@Component
@Primary
public class DemoDataPermissionUtil implements IDataPermissionUtil {
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("userIds", new String[]{"-1", "-2"});
        return result;
    }

    @Override
    public Boolean isSkip() {
        return false;
    }
}
