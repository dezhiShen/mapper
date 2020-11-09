package cn.dezhishen.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import cn.dezhishen.core.config.DataPermissionProperties;
import cn.dezhishen.core.constant.Constant;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author shendezhi
 */
@Intercepts(value =
        {
                @Signature(type = Executor.class, method = "query",
                        args = {
                                MappedStatement.class,
                                Object.class,
                                RowBounds.class,
                                ResultHandler.class,
                                CacheKey.class,
                                BoundSql.class
                        }),
                @Signature(type = Executor.class, method = "query",
                        args = {
                                MappedStatement.class,
                                Object.class,
                                RowBounds.class,
                                ResultHandler.class,
                        }),
                @Signature(type = Executor.class, method = "update",
                        args = {
                                MappedStatement.class,
                                Object.class
                        })
        })
public class DataPermissionInterceptor implements Interceptor {
    /**
     * 配置文件
     */
    private DataPermissionProperties permissionProperties;
    /**
     * 查询权限标识的实现类,需要外部注入
     */
    private IDataPermissionUtil dataPermissionUtil;

    public void setPermissionProperties(DataPermissionProperties permissionProperties) {
        this.permissionProperties = permissionProperties;
    }

    public void setDataPermissionUtil(IDataPermissionUtil dataPermissionUtil) {
        this.dataPermissionUtil = dataPermissionUtil;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        //处理参数
        invocation.getArgs()[1] = processParameter(ms, parameter);
        return invocation.proceed();
    }

    /**
     * 处理 sql的 参数,增加权限全局动态参数
     *
     * @param ms        MappedStatement
     * @param parameter 参数
     * @return 处理后的参数
     */
    private MapperMethod.ParamMap<Object> processParameter(MappedStatement ms, Object parameter) {
        MapperMethod.ParamMap<Object> result;
        Map<String, Object> dataPermissions;
        if (permissionProperties.getIgnored() != null && permissionProperties.getIgnored().contains(ms.getId())) {
            //忽略,则将数据权限设置为跳过
            dataPermissions = new HashMap<>();
            dataPermissions.put(Constant.IS_SKIP_PARAMETER_NAME, true);
        } else {
            dataPermissions = dataPermissionUtil.getAll();
            dataPermissions.put(Constant.IS_SKIP_PARAMETER_NAME, dataPermissionUtil.isSkip());
        }
        if (parameter == null) {
            result = new MapperMethod.ParamMap<>();
            result.putAll(dataPermissions);
        } else if (parameter instanceof MapperMethod.ParamMap) {
            //noinspection unchecked
            result = (MapperMethod.ParamMap<Object>) parameter;
            result.putAll(dataPermissions);
        } else if (ClassUtils.isPrimitiveOrWrapper(parameter.getClass())
                || String.class.isAssignableFrom(parameter.getClass())
                || Number.class.isAssignableFrom(parameter.getClass())) {
            String paramName = permissionProperties.getSingleParamName(ms.getId());
            result = new MapperMethod.ParamMap<>();
            result.putAll(dataPermissions);
            result.put(paramName, parameter);
        } else {
            JSONObject jsonObject = JSONObject.parseObject(
                    JSONObject.toJSONString(
                            parameter,
                            SerializerFeature.WriteMapNullValue,
                            SerializerFeature.UseISO8601DateFormat
                    ),
                    Feature.AllowISO8601DateFormat);
            result = new MapperMethod.ParamMap<>();
            result.putAll(dataPermissions);
            result.putAll(jsonObject);
        }
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
