package cn.dezhishen.core.mapper.batch;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * @author shendezhi
 */
public class BaseBatchInsertProvider extends MapperTemplate {

    public BaseBatchInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据对象生成批量插入mybatis的sql模板
     *
     * @param ms
     * @return
     */
    public String batchInsert(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append("VALUES");
        sql.append("<foreach collection =\"list\" item=\"item\" separator=\",\" >");
        sql.append("(");
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        StringBuilder cols = new StringBuilder();
        for (EntityColumn column : columnSet) {
            if (!column.isInsertable()) {
                continue;
            }
            cols.append("<choose>" + "<when test=\"item.")
                    .append(column.getProperty())
                    .append("!=null\">").append("#{item.").append(column.getProperty())
                    .append("}")
                    .append("</when>")
                    .append("<otherwise>")
                    .append("null")
                    .append("</otherwise>")
                    .append("</choose>")
                    .append(",");
        }
        sql.append(cols.substring(0, cols.length() - 1));
        sql.append(")");
        sql.append("</foreach>");
        return sql.toString();
    }
}
