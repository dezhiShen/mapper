package cn.dezhishen.core.config;

import cn.dezhishen.core.constant.Constant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * 数据权限的配置
 *
 * @author shendezhi
 */
@Configuration
@ConfigurationProperties(prefix = Constant.DATA_PERMISSION_PROPERTIES_PREFIX)
public class DataPermissionProperties {
    private Boolean enabled;
    private Map<String, String> singleParamNames;
    private Set<String> ignored;
    private String defaultSingleParamName = "id";

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getSingleParamNames() {
        return singleParamNames;
    }

    public void setSingleParamNames(Map<String, String> singleParamNames) {
        this.singleParamNames = singleParamNames;
    }

    public String getDefaultSingleParamName() {
        return defaultSingleParamName;
    }

    public void setDefaultSingleParamName(String defaultSingleParamName) {
        this.defaultSingleParamName = defaultSingleParamName;
    }

    public String getSingleParamName(String msId) {
        if (singleParamNames == null || singleParamNames.isEmpty()) {
            return defaultSingleParamName;
        }
        return singleParamNames.getOrDefault(msId, defaultSingleParamName);
    }

    public Set<String> getIgnored() {
        return ignored;
    }

    public void setIgnored(Set<String> ignored) {
        this.ignored = ignored;
    }
}
