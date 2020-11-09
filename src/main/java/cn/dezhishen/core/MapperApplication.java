package cn.dezhishen.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author shendezhi
 */
@SpringBootApplication
@MapperScan(basePackages = {"cn.dezhishen.core"})
public class MapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapperApplication.class, args);
    }
}
