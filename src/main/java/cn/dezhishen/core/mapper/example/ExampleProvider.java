package cn.dezhishen.core.mapper.example;

import cn.dezhishen.core.annotations.DataPermission;
import cn.dezhishen.core.annotations.ParamType;
import cn.dezhishen.core.constant.Constant;
import cn.dezhishen.core.mapper.util.WhereUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.util.Set;

public class ExampleProvider extends MapperTemplate {
    public ExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectCountByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append(SqlHelper.exampleCountColumn(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(WhereUtil.exampleWhereClause(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    /**
     * 根据Example删除
     *
     * @param ms
     * @return
     */
    public String deleteByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //如果设置了安全删除，就不允许执行不带查询条件的 delete 方法
        if (getConfig().isSafeDelete()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("_parameter"));
        }
        if (SqlHelper.hasLogicDeleteColumn(entityClass)) {
            sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
            sql.append("<set>");
            sql.append(SqlHelper.logicDeleteColumnEqualsValue(entityClass, true));
            sql.append("</set>");
            MetaObjectUtil.forObject(ms).setValue("sqlCommandType", SqlCommandType.UPDATE);
        } else {
            sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        }
        sql.append(WhereUtil.exampleWhereClause(entityClass));
        return sql.toString();
    }


    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(WhereUtil.exampleWhereClause(entityClass));
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectByExampleAndRowBounds(MappedStatement ms) {
        return selectByExample(ms);
    }


    public static String updateByExampleWhereClause(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> allColumn = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : allColumn) {
            //权限字段
            if (column.getEntityField().getAnnotation(DataPermission.class) != null) {
                DataPermission dataPermission = column.getEntityField().getAnnotation(DataPermission.class);
                sql.append("<if test=\"!" + Constant.IS_SKIP_PARAMETER_NAME + "\">");
                sql.append(" AND ")
                        .append(column.getColumn());
                if (ParamType.List.equals(dataPermission.paramType())) {
                    sql.append(" in ").append("<foreach collection=")
                            .append("\"").append(dataPermission.field()).append("\" ")
                            .append("open=\"(\" close=\")\" item=\"item\" separator=\",\">")
                            .append("#{item}").append("</foreach>");
                } else {
                    sql.append("=#{\"").append(dataPermission.field()).append("\"}");
                }
                sql.append("</if>");
            }
        }
        sql.append(
                " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(example)}" +
                        " <trim prefix=\" AND (\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                        "  <foreach collection=\"example.oredCriteria\" item=\"criteria\">\n" +
                        "    <if test=\"criteria.valid\">\n" +
                        "      ${@tk.mybatis.mapper.util.OGNL@andOr(criteria)}" +
                        "      <trim prefix=\"(\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                        "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                        "          <choose>\n" +
                        "            <when test=\"criterion.noValue\">\n" +
                        "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
                        "            </when>\n" +
                        "            <when test=\"criterion.singleValue\">\n" +
                        "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value}\n" +
                        "            </when>\n" +
                        "            <when test=\"criterion.betweenValue\">\n" +
                        "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
                        "            </when>\n" +
                        "            <when test=\"criterion.listValue\">\n" +
                        "              ${@tk.mybatis.mapper.util.OGNL@andOr(criterion)} ${criterion.condition}\n" +
                        "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
                        "                #{listItem}\n" +
                        "              </foreach>\n" +
                        "            </when>\n" +
                        "          </choose>\n" +
                        "        </foreach>\n" +
                        "      </trim>\n" +
                        "    </if>\n" +
                        "  </foreach>\n" +
                        " </trim>");
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 根据Example更新非null字段
     *
     * @param ms
     * @return
     */
    public String updateByExampleSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //安全更新，Example 必须包含条件
        if (getConfig().isSafeUpdate()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("example"));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumnsIgnoreVersion(entityClass, "record", true, isNotEmpty()));
        sql.append(updateByExampleWhereClause(entityClass));
        return sql.toString();
    }

    /**
     * 根据Example更新
     *
     * @param ms
     * @return
     */
    public String updateByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        //安全更新，Example 必须包含条件
        if (getConfig().isSafeUpdate()) {
            sql.append(SqlHelper.exampleHasAtLeastOneCriteriaCheck("example"));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass), "example"));
        sql.append(SqlHelper.updateSetColumnsIgnoreVersion(entityClass, "record", false, false));
        sql.append(updateByExampleWhereClause(entityClass));
        return sql.toString();
    }

    /**
     * 根据Example查询一个结果
     *
     * @param ms
     * @return
     */
    public String selectOneByExample(MappedStatement ms) {
        return selectByExample(ms);
    }


}
