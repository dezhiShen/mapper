package cn.dezhishen.core.mapper.base.delete;

import cn.dezhishen.core.mapper.util.WhereUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

public class BaseDeleteProvider extends MapperTemplate {
    public BaseDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过条件删除
     *
     * @param ms
     * @return
     */
    public String delete(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //如果设置了安全删除，就不允许执行不带查询条件的 delete 方法
        if (getConfig().isSafeDelete()) {
            sql.append(SqlHelper.notAllNullParameterCheck("_parameter", EntityHelper.getColumns(entityClass)));
        }
        // 如果是逻辑删除，则修改为更新表，修改逻辑删除字段的值
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append(WhereUtil.whereAllIfColumns(entityClass,isNotEmpty()));
        return sql.toString();
    }

    /**
     * 通过主键删除
     *
     * @param ms
     */
    public String deleteByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append(WhereUtil.wherePKColumns(entityClass));
        return sql.toString();
    }
}
