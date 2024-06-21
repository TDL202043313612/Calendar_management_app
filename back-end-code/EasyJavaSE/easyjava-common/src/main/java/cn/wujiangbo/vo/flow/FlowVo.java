package cn.wujiangbo.vo.flow;

import lombok.Data;

/**
 * <p>首页流程节点对象</p>
 *
 */
@Data
public class FlowVo {

    private String name;//节点名称
    private String state;//0：暂停传输；1：传输中
}
