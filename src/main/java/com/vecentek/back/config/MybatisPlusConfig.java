package com.vecentek.back.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus的配置类，包括分页插件、防止全表操作的拦截器、自定义Mybatis配置等。
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 13:58
 */

@Configuration
@MapperScan({"com.vecentek.back.mapper"})
@EnableTransactionManagement
public class MybatisPlusConfig {


    /**
     * 防止修改与删除时对全表进行操作
     *
     * @return {@link BlockAttackInnerInterceptor}
     * @author EdgeYu
     * @date 2023-03-28 15:40
     */
    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }


    /**
     * 新的分页插件,一缓和二缓遵循 mybatis 的规则,需要设置 MybatisConfiguration useDeprecatedExecutor = false 避免缓存出现问题
     *
     * @return {@link MybatisPlusInterceptor}
     * @author EdgeYu
     * @date 2023-03-28 15:39
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    /**
     * ConfigurationCustomizer,这里引用的是 MyBatisPlus 自定义的一个和 MyBatis 同名的接口 ConfigurationCustomizer
     * 因此必须使用 MyBatisPlus 的 ConfigurationCustomizer
     *
     * @return {@link ConfigurationCustomizer}
     * @author EdgeYu
     * @date 2023-03-28 15:40
     */
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setCacheEnabled(true);
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setCallSettersOnNulls(true);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
        };
    }

}