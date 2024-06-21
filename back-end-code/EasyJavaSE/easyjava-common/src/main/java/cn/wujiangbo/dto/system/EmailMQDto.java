package cn.wujiangbo.dto.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>邮件消息的实体类</p>
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailMQDto implements Serializable {

    private List<String> emailAddress;//目标邮箱集合
    private String emailContent;//邮件内容
}