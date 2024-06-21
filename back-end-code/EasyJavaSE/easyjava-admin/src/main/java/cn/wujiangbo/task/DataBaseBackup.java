package cn.wujiangbo.task;

import cn.wujiangbo.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>数据库备份定时任务</p>
 *
 */
@Component
@Slf4j
public class DataBaseBackup {

    /**
     * 数据库版本是否为 8.0 + （false=否  true=是）， mysql8+ 需要参数  --column-statistics=0  ， mysql8- 不需要
     */
    Boolean isDbVersion8 = false;

    /**
     * 备份命令
     * USERNAME   账号
     * PASSWORD   密码
     * SERVERPATH 服务器IP/域名
     * DBNAME     数据库名称
     * FILEPATH   备份文件存放地址+名称
     * 说明
     * cmdCompression ： 需压缩 （本地或服务器需安装 mysqldump 命令(安装mysql自带患独立安装) +  gzip 命令(独立安装)）
     * cmd ：            不压缩 (本地或服务器需安装 mysqldump 命令(安装mysql自带患独立安装)
     * --column-statistics=0     mysql8 添加该参数, 非mysql8 不添加, 否则将出错
     */
    String cmdMysql8 = " --column-statistics=0  -u{USERNAME} -p{PASSWORD} -h{SERVERPATH} -P3306 --databases {DBNAME}"; //  > {FILEPATH}.sql
    String cmd = " -u{USERNAME} -p{PASSWORD} -h{SERVERPATH} -P3306 --databases {DBNAME}";      //  > {FILEPATH}.sql

    //是否定时备份数据库（0：不备份；1：备份）
    @Value("${easyjava.backup_flag}")
    private String backup_flag;

    @Value("${easyjava.data_base}")
    private String dbName;

    //需要备份的目标数据库账号
    @Value("${easyjava.user_name}")
    private String user_name;

    //需要备份的目标数据库密码
    @Value("${easyjava.pass_word}")
    private String pass_word;

    //备份文件存储目录
    @Value("${easyjava.save_path}")
    private String save_path;

    //需要备份的目标数据库IP地址
    @Value("${easyjava.host_ip}")
    private String hostIp;

    //备份文件的个数
    @Value("${easyjava.sql_count}")
    private int sql_count;

    //mysqldump所在目录的路径
    @Value("${easyjava.dump_path}")
    private String dumpPath;

    /**
     * 数据库备份任务，编号：S1001
     */
//    @Scheduled(cron = "0 30 23 ? * *")//每天晚上11点半触发
    //@Scheduled(cron = "0 30 23 ? * *")//每天晚上11点半触发
    //@Scheduled(cron = "0/30 * * * * ?")//每隔30秒执行一次-测试使用
    @Scheduled(cron = "0 0/30 * * * ?")//每隔30分钟执行一次
    public void task001() {
        if ("1".equals(backup_flag)) {
            log.info("--------【定时任务-S1001:数据库备份-每隔30分钟触发一次】-------------执行开始，时间：{}", DateUtils.getCurrentDateString());
            try {
                doJob();
            } catch (Exception e) {
                log.info("【定时任务-S1001:数据库备份】执行时，发生异常：" + e.getLocalizedMessage());
            }
            log.info("--------【定时任务-S1001:数据库备份】-------------执行结束");

            /**
             * 检查备份文件个数，如超出 sql_count 个，则删除最开始备份的文件，依次类推，该文件夹下最多存在 sql_count 个备份文件
             */
            File file = new File(save_path);
            File fileDb[] = file.listFiles();
            if(fileDb != null && fileDb.length > sql_count){
                log.info("数据库已备份文件的总个数：{}，开始删除旧备份数据", fileDb.length);
                List<File> fileList = Arrays.asList(fileDb);
                //升序排序后删除第一个文件
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return String.valueOf(o1.lastModified()).compareTo(String.valueOf(o2.lastModified()));
                    }
                });
                fileList.get(0).delete();//删除
                log.info("删除旧备份数据完毕");
            }
        }else{
            log.info("【定时任务-数据库备份】不执行");
        }
    }

    public void doJob(){
        // 备份文件目录+名称  备份文件存放目录+名称(名称 = 数据库名+时间字符串.sql)
        String timeStr = DateUtils.getCurrentDateString(DateUtils.YYYYMMDDHHMMSS);
        String pathFileName = save_path + dbName + "_" + timeStr + ".sql";
        String newCmd = "";
        if (isDbVersion8) {
            newCmd = cmdMysql8;
        } else {
            newCmd = cmd;
        }
        // 执行命令
        newCmd =  newCmd.replace("{USERNAME}", user_name)
                .replace("{PASSWORD}", pass_word)
                .replace("{SERVERPATH}", hostIp)
                .replace("{DBNAME}", dbName)
                .replace("{FILEPATH}", pathFileName);
        newCmd = dumpPath + " " + newCmd;
        System.out.println("数据库备份命令=" + newCmd);
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            // 创建存放sql的文件
            existsFile(new File(pathFileName));
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(pathFileName), "utf8"));
            Process process = null;
            String property = System.getProperty("os.name");
            System.out.println("os.name=" + property);
            if (property.indexOf("Linux") != -1) {
                // linux
                process = Runtime.getRuntime().exec(new String[]{"bash", "-c", newCmd});
            } else {
                // 本地win
                process = Runtime.getRuntime().exec(newCmd);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            // 此次会执行过长时间,直到备份完成
            printWriter.flush();
            printWriter.close();
            //0 表示线程正常终止。
            if (process.waitFor() == 0) {
                // 线程正常执行
                log.info("【备份数据库】成功，SQL文件路径：{}", pathFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【备份数据库】失败：{}", e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件是否存在，不存在创建
     */
    private static void existsFile(File file) {
        // 判断文件路径是否存在,不存在新建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}