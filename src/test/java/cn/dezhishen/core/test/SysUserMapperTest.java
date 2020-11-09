package cn.dezhishen.core.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.dezhishen.core.MapperApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author shendezhi
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MapperApplication.class)
public class SysUserMapperTest {
    @Autowired
    private SysUserMapper userMapper;

    @Test
    public void get() {
        SysUser user = new SysUser();
        user.setUserId("1");
        SysUser result = userMapper.selectByPrimaryKey(user);
        System.out.println(result);
    }

    @Test
    public void list() {
        List<SysUser> result = userMapper.select(new SysUser());
        System.out.println(result);
    }

    @Test
    public void query() {
        PageInfo<SysUser> userPageInfo = PageHelper.startPage(1, 1).doSelectPageInfo(
                () -> userMapper.select(new SysUser())
        );
        System.out.println(userPageInfo);
    }

    @Test
    public void selectById() {
        SysUser result = userMapper.selectById("1");
        System.out.println(result);
    }

    @Test
    public void selectByIdForIgnored() {
        SysUser result = userMapper.selectByIdForIgnored("1");
        System.out.println(result);
    }

    @Test
    public void selectByExample() {
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("userId", "1");
        userMapper.selectByExample(example);
        example = new Example(SysUser.class);
        userMapper.selectByExample(example);
    }

    @Test
    @Rollback
    public void updateByExample() {
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("userId", "-1");
        SysUser record = new SysUser();
        record.setUserId("-1");
        userMapper.updateByExampleSelective(record, example);
    }

    @Test
    @Rollback
    public void deleteByExample() {
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("userId", "-1");
        userMapper.deleteByExample(example);
    }

    @Test
    @Rollback
    public void deleteByPrimaryKey() {
        SysUser record = new SysUser();
        record.setUserId("-1");
        userMapper.deleteByPrimaryKey(record);
    }

    @Test
    @Rollback
    public void delete() {
        SysUser record = new SysUser();
        record.setUserId("-1");
        userMapper.delete(record);
    }

    @Test
    @Rollback
    public void updateByPk() {
        SysUser sysUser = new SysUser();
        sysUser.setUserId("-1");
        userMapper.updateByPrimaryKey(sysUser);
        userMapper.updateByPrimaryKeySelective(sysUser);
    }
}
