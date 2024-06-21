package cn.wujiangbo.result;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果集对象
 *
 */
@Data
public class PageList<T> {

    private long total;
    private List<T> rows = new ArrayList<>();

    @Override
    public String toString() {
        return "PageList{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }

    //提供有参构造方法，方便测试
    public PageList(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
    //除了有参构造方法，还需要提供一个无参构造方法
    public PageList() {
    }
}