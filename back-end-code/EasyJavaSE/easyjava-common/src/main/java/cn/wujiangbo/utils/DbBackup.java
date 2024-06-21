package cn.wujiangbo.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.*;

/**
 * @Description: 数据库还原
 *
 */
@Slf4j
public class DbBackup {

    //恢复mysql数据库
    public static void restoreMysqlDB(String sql_path, String mysql_db_restore) {
        if(StringUtils.isBlank(sql_path) || StringUtils.isBlank(mysql_db_restore)){
            log.error("数据库恢复无法执行，sql_path或mysql_db_restore为空，请检查");
        }else{
//            String command = "cmd /c mysql -h localhost -uroot -pcolin123 easy9 < D://Easy9//easy9.sql";
            String command = mysql_db_restore + " < " + sql_path;
            try {
                Process process = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                log.error("数据库恢复时发生异常:{}", e.getLocalizedMessage());
            }
        }

    }

    //备份mysql数据库
    public static void backupMysqlDB(String mysql_db_backup, String mysql_db_backup_file) {
//        String command = "cmd /c mysqldump -uroot -pcolin123 easy9 > D://easy9.sql";
        if(StringUtils.isBlank(mysql_db_backup) || StringUtils.isBlank(mysql_db_backup_file)){
            log.error("数据库备份无法执行，mysql_db_backup或mysql_db_backup_file为空，请检查");
        }else{
            //如果文件夹不存在，则创建
            File file = new File(mysql_db_backup_file);
            if(!file.exists()){
                file.mkdirs();
            }
            //检查备份文件个数，如超出30个，则删除最开始备份的文件，依次类推，该文件夹下最多存在30个备份文件
            File fileDb[] = file.listFiles();
            if(fileDb != null && fileDb.length > 30){
                log.info("数据库已备份文件的总个数：{}", fileDb.length);
                List<File> fileList = Arrays.asList(fileDb);
                //升序排序后删除第一个文件
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return String.valueOf(o1.lastModified()).compareTo(String.valueOf(o2.lastModified()));
                    }
                });
                fileList.get(0).delete();//删除
            }
            //生成备份文件名
            String backupFileName = DateUtils.dateTimeNow() + ".sql";//当前时间戳
            String command = mysql_db_backup + " > " + mysql_db_backup_file + "//" + backupFileName;
            try {
                log.info("开始执行数据库备份命令：{}", command);
                Process process = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                log.error("数据库备份时发生异常:{}", e.getLocalizedMessage());
            }
        }
    }
}
