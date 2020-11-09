package cn.dezhishen.core.mapper.base.select;

import cn.dezhishen.core.mapper.util.WhereUtil;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * @author shendezhi
 */
public class BaseSelectProvider extends MapperTemplate {

    public BaseSelectProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }


    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String selectOne(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        return SqlHelper.selectAllColumns(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                WhereUtil.whereAllIfColumns(entityClass, isNotEmpty());
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String select(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        return SqlHelper.selectAllColumns(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                WhereUtil.whereAllIfColumns(entityClass, isNotEmpty()) +
                SqlHelper.orderByDefault(entityClass);
    }

    /**
     * 查询
     *
     * @param ms
     * @return
     */
    public String selectByRowBounds(MappedStatement ms) {
        return select(ms);
    }

    /**
     * 根据主键进行查询
     *
     * @param ms
     */
    public String selectByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        return SqlHelper.selectAllColumns(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                WhereUtil.wherePKColumns(entityClass);
    }

    /**
     * 查询总数
     *
     * @param ms
     * @return
     */
    public String selectCount(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        return SqlHelper.selectCount(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                WhereUtil.whereAllIfColumns(entityClass, isNotEmpty());
    }

    /**
     * 根据主键查询总数
     *
     * @param ms
     * @return
     */
    public String existsWithPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        return SqlHelper.selectCountExists(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                SqlHelper.wherePKColumns(entityClass);
    }

    /**
     * 查询全部结果
     *
     * @param ms
     * @return
     */
    public String selectAll(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        return SqlHelper.selectAllColumns(entityClass) +
                SqlHelper.fromTable(entityClass, tableName(entityClass)) +
                // 逻辑删除的未删除查询条件
                "<where>" +
                SqlHelper.whereLogicDelete(entityClass, false) +
                "</where>" +
                SqlHelper.orderByDefault(entityClass);
    }
}
