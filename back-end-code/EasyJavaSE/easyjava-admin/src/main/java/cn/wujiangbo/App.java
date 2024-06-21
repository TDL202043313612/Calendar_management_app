package cn.wujiangbo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动类
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("******************** 项目开始启动 ********************");

        ConfigurableApplicationContext application = SpringApplication.run(App.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        // 未配置默认空白
        if(path == null){
            path = "";
        }

        System.out.println("\n----------------------------------------------------------\n\t" +
                "系统启动成功，访问路径如下:\n\t" +
                "本地路径: \thttp://localhost:" + port + path + "/\n\t" +
                "网络地址: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "API文档: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n" +
                "----------------------------------------------------------");
    }
}