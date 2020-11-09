# 持久层 core

通用Mapper


## 功能
### 支持PageHelper分页
### 支持通用Mapper
* 1.基类

基于如下项目进行封装
```
<groupId>tk.mybatis</groupId>
<artifactId>mapper-spring-boot-starter</artifactId>
```
* 2.拓展方法
    * 2.1.batchInsert
    
        此方法传入List<T>,**且空值,会插入空,而非默认值**
    * 2.2.exists
        
        此方法 为 limit 1 查询,而非count查询,理论上能提高一定查询速度
### 通用Mapper封装数据权限
* 1.通过mybatis拦截器,增加全局权限参数设置
    * 1.1.需要外部自行实现接口 
    
    `com.dezhishen.core.interceptor.IDataPermissionUtil`
    * 1.2.需要在配置文件中声明 
    ```
        data-permission:
            ...
            enabled: true
    ```
* 2.通用查询中,增加了注解`com.dezhishen.core.annotations.@DataPermission`,用于标识权限的条件和信息
## 配置
### 说明
* 以yaml为例,前缀为`data-permission`
* 对象 `com.dezhishen.core.config.DataPermissionProperties`
* 属性说明

名称|类型|默认值|说明
-|-|-|-
enabled|boolean|false|是否启用数据权限
ignored|Set\<String\>|null|忽略的查询,完整的方法名,<br>如`com.xxx.mapper.TestMapper.selectPublic`
single-param-names|Map\<String,String\>|null|部分Mapper层的方法为单一参数,且未通过`@Param`设置`mybatis`的参数名称,<br>拦截器中无法正确设置参数名称,<br>所以通过此处进行设置,<br>如`{"com.xxx.mapper.TestMapper.selectByCode":"code"}`,<br>则`mapper.xml`中`sql`为`select ... from table where code = #{code} and ...`
default-single-param-name|String|id|部分Mapper层的方法为单一参数,<br>且未通过`@Param`设置`mybatis`的参数名称,<br>而且未设置`single-param-names`时,<br>采用此值作为参数名

### 示例
```
data-permission:
    enabled: true # 是否启用
    igored:
        - com.xxx...XxxMapper.selectxxx
    single-param-names: # Map<String,String>
        com.xxx.xx.Mapper.functionName: 'code'
    default-single-param-name: id # 当mybatis的Mapper中,只有1个传入参数,且没有指定`@Param(name=?)`时,
```

## 使用
### PageHelper
通用分页组件,通PageHelper保持一致

```
PageHelper.startPage(pageNum,pageSize).xxx().xxx().doSelectPageInfo(
    () -> xxxMapper.select(new T())
)
```

### 通用Mapper
```
public interface XxxMapper extends Mapper<Xxx>{
    
} 
```
### 示例
#### 通用查询
* 如:
    * 实体对象 `SysUser` 需要 对 `userId` 进行过滤
    * 权限工具类针对每个用户返回如下
        ```
        {
            "userIds":["id1","id2"...]
        }
        ```
        * `isAdmin()` 如果返回`true`,则说明为管理员,不调整数据权限  
    ```
      public class SysUser{
        ...
        @DataPermission(field = "userIds", paramType = ParamType.List)
        private String userId;
      }
    ```
    * SysUserMapper
    ```
        public interface SysUserMapper extends Mapper<SysUser>{
            ...
        }
    ```
    * 调用
        ```
            @Service
            public class TestService{
                @Autowired
                private SysUserMapper userMapper;
      
                public List<SysUser> select(SysUser condition){
                    return userMapper.select(condition)
                }
            }
        ```
    生成的sql为:
        ```
            select .... from sys_user where ... and user_id in ("id1"....)
        ```
#### 自定义sql查询
* `SysUserMapper.java`
```
...
    SysUser selectById(String id)
```
* `application.yaml`
data-permission:
  ....
  single-param-names:
    "com.dezhishen.core.test.SysUserMapper.selectById": **"`userId`"**

* 在`mapper.xml`中,可以直接使用`权限工具类`返回对象,如:
```
    select * from sys_user 
     where user_id = #{userId} 
    <if test="!_isSkip_">
       and user_id in 
       <foreach collection="userIds" item="_id" open="(" close=")" separator=",">
           #{_id}
       </foreach>
    </if>
```
**ps:此处声明了单一参数时,参数名为`userId`**
####
#### demo
[SysUserMapperTest](.src/test/java/cn/dezhishen/core/test/SysUserMapperTest.java)

## todo
- [x] 整合PageHelper
- [x] 基础查询实现数据权限
- [x] 管理员标识判断接口
- [x] ExampleMapper
- [x] UpdateMapper
- [x] DeleteMapper