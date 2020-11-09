package cn.dezhishen.core.mapper.exists;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * @author shendezhi
 */
public class BaseExistsProvider extends MapperTemplate {
    public BaseExistsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据对象生成判断 符合条件的记录 是否存在的mybatis的sql模板
     *
     * @param ms
     * @return
     */
    public String exists(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        return "select count(1) from (select 1 " +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()) +
                "limit 1)t";
    }
}
