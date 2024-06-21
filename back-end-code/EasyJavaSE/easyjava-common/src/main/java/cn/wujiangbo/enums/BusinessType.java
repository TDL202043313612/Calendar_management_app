package cn.wujiangbo.enums;

/**
 * 业务操作类型
 *
 */
public enum BusinessType
{
    OTHER(0, "其他"),

    INSERT(1, "新增"),

    UPDATE(2, "修改"),

    DELETE(3, "删除"),

    QUERY(4, "查询"),

    GRANT(5, "授权"),

    EXPORT(6, "导出"),

    IMPORT(7, "导入"),

    CLEAN(8, "清空数据");

    private final Integer code;
    private final String info;

    BusinessType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}