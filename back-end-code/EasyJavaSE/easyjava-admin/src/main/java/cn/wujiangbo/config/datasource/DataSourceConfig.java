package cn.wujiangbo.config.datasource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
@MapperScan("cn.wujiangbo.mapper")
public class DataSourceConfig {

    /*
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(500);// 设置最大分页限制为 500 条记录
        paginationInterceptor.setOverflow(true); // 开启物理分页
        paginationInterceptor.setDialect(null); // 关闭 SQL 解析优化
        paginationInterceptor.setDbType(DbType.MYSQL); // 设置数据库类型为 MySQL
        // 开启 count 的 join 优化，只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}