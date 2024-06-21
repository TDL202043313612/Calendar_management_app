package cn.wujiangbo.utils;

import cn.wujiangbo.query.base.BaseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>Page分页工具类</p>
 *
 */
public class PageTools {

    private PageTools() {}

    private static class PageToolsSingletonHolder {
        private static final PageTools INSTANCE = new PageTools();
    }

    public static PageTools getInstance() {
        return PageToolsSingletonHolder.INSTANCE;
    }

    public Page getPage(BaseQuery baseQuery){
        Page page = new Page<>(baseQuery.getCurrent(), baseQuery.getSize());
        //关闭count sql的优化，解决前端查询慢的效率问题
        page.setOptimizeCountSql(false);
        // 是否统计总结果记录数
        page.setSearchCount(true);
        return page;
    }
}
