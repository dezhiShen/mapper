package cn.dezhishen.core.mapper.util;

import cn.dezhishen.core.annotations.DataPermission;
import cn.dezhishen.core.annotations.ParamType;
import cn.dezhishen.core.constant.Constant;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

public class WhereUtil {
    public static String exampleWhereClause(Class<?> entityClass) {
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
        sql.append("<if test=\"_parameter != null\"> " +
                " ${@tk.mybatis.mapper.util.OGNL@andNotLogicDelete(_parameter)}" +
                " <trim prefix=\" AND (\" prefixOverrides=\"and |or \" suffix=\")\">\n" +
                "  <foreach collection=\"oredCriteria\" item=\"criteria\">\n" +
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
                " </trim>\n" +
                "</if>");
        sql.append("</where>");
        return sql.toString();
    }

    public static String wherePKColumns(Class<?> entityClass) {
        return wherePKColumns(entityClass, false);
    }

    public static String wherePKColumns(Class<?> entityClass, boolean useVersion) {
        return wherePKColumns(entityClass, null, useVersion);
    }

    /**
     * where主键条件
     *
     * @param entityClass
     * @param entityName
     * @param useVersion
     * @return
     */
    public static String wherePKColumns(Class<?> entityClass, String entityName, boolean useVersion) {
        StringBuilder sql = new StringBuilder();
        boolean hasLogicDelete = SqlHelper.hasLogicDeleteColumn(entityClass);

        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {
            sql.append(" AND ").append(column.getColumnEqualsHolder(entityName));
        }
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
        if (useVersion) {
            sql.append(SqlHelper.whereVersion(entityClass));
        }
        if (hasLogicDelete) {
            sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        }
        sql.append("</where>");
        return sql.toString();
    }

    public static String whereAllIfColumns(Class<?> entityClass) {
        return whereAllIfColumns(entityClass, false);
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty) {
        return whereAllIfColumns(entityClass, empty, false);
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty, boolean useVersion) {
        StringBuilder sql = new StringBuilder();
        boolean hasLogicDelete = false;
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {
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
            if (!useVersion || !column.getEntityField().isAnnotationPresent(Version.class)) {
                // 逻辑删除，后面拼接逻辑删除字段的未删除条件
                if (logicDeleteColumn != null && logicDeleteColumn == column) {
                    hasLogicDelete = true;
                    continue;
                }
                sql.append(SqlHelper.getIfNotNull(column, " AND " + column.getColumnEqualsHolder(), empty));
            }
        }
        if (useVersion) {
            sql.append(SqlHelper.whereVersion(entityClass));
        }
        if (hasLogicDelete) {
            sql.append(SqlHelper.whereLogicDelete(entityClass, false));
        }
        sql.append("</where>");
        return sql.toString();
    }
}
