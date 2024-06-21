package cn.wujiangbo.query.app;

import cn.wujiangbo.query.base.BaseQuery;
import lombok.Data;

/**
 * @desc 新闻信息表-查询对象
 * @author bobo(weixin:javabobo0513)
 * @since 2024-04-30
 */
@Data
public class AppNewsQuery extends BaseQuery{

    private String newsType;//新闻类型
}