package cn.dezhishen.core.config;

import cn.dezhishen.core.interceptor.DataPermissionInterceptor;
import cn.dezhishen.core.interceptor.IDataPermissionUtil;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author shendezhi
 */
@Configuration
public class InterceptorConfig {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @Autowired
    private DataPermissionProperties dataPermissionProperties;

    @Autowired
    private PageHelperProperties pageHelperProperties;

    @Autowired
    @Lazy
    private IDataPermissionUtil dataPermissionUtil;

    @PostConstruct
    public DataPermissionInterceptor addInterceptor() {
        DataPermissionInterceptor interceptor = null;
        if (dataPermissionProperties.getEnabled()) {
            if (dataPermissionUtil == null) {
                throw new RuntimeException("启用了权限拦截器,单未配置 IDataPermissionUtil,请实现 IDataPermissionUtil 接口");
            }
            interceptor = new DataPermissionInterceptor();
            interceptor.setPermissionProperties(dataPermissionProperties);
            interceptor.setDataPermissionUtil(dataPermissionUtil);
        }
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.putAll(this.pageHelperProperties.getProperties());
        pageInterceptor.setProperties(properties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
            sqlSessionFactory.getConfiguration().addInterceptor(pageInterceptor);
            if (dataPermissionProperties.getEnabled()) {
                sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
            }
        }
        return interceptor;
    }
}
