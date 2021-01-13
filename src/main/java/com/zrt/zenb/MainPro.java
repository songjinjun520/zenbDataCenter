/**
 *
 */
package com.zrt.zenb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;
import com.zrt.zenb.controller.DevController;
import com.zrt.zenb.controller.service.TcpClient;
import com.zrt.zenb.entity.LogMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zrt.zenb.common.DataCenter;
import com.zrt.zenb.common.DateUtil;
import com.zrt.zenb.common.FileUtils;
import com.zrt.zenb.dao.ZenbDao;
import com.zrt.zenb.service.DZUdpService;
import com.zrt.zenb.service.DataService;
import com.zrt.zenb.service.TcpServer;
import com.zrt.zenb.service.ZenbUdpService;

/**
 * @author PGW
 *
 */
@Slf4j
public final class MainPro extends ZenbBase {

    public static MainPro main;

    public static ZenbDao dao;

    private ExecutorService exePool;

    /**
     * 系统启动时间
     * 界面查看实时数据时以该时间节点为起点
     */
    public static int sysStartTime = 0;

    /**
     * 全数字时间秒数正则验证
     */
    static String numberRegex = "^[0-9-]+$";
    static Pattern numberPattern = Pattern.compile(numberRegex);

    private TcpServer tcpServer;

    /**
     *
     */
    private MainPro() {
        Date nowDate = new Date();

        sysStartTime = (int) (nowDate.getTime() / 1000);
//        	sysStartTime = 1570751387;
        log.info("sys start at {}", sysStartTime);
        try {
            ApplicationContext ac = new FileSystemXmlApplicationContext("/applicationContext_mysql.xml");
            dao = (ZenbDao) ac.getBean("netDao");
//                        dao = ZenbDao.getDao();
            initProperties();
////                        dao.initDb(dbIp, username, password);
//                        dao.openDao();

            DevController devController = new DevController();
            devController.initDevInfos();
            DataCenter.devController = devController;

            exePool = Executors.newFixedThreadPool(6);

            DataService dataService = new DataService(dataPath, targetPath, checkInterval);
            exePool.execute(dataService);

            tcpServer = new TcpServer(4028);
            exePool.execute(tcpServer);

            TcpClient tcpClient = new TcpClient();
            DataCenter.tcpClient = tcpClient;
            exePool.execute(tcpClient);

//                        new LteDZConnectUdpService();
            new DZUdpService();
            new ZenbUdpService();
            // 开机时将data目录下时间毫秒数目录删除掉
            if (deleteDirOnStart) {
                File baseDir = new File(dataPath);
                File[] dirs = baseDir.listFiles();
                String dirName = "";
                for (File dir : dirs) {
                    if (!dir.isDirectory()) {
                        continue;
                    }

                    dirName = dir.getName();
                    // 进入时间秒数目录
                    Matcher match = numberPattern.matcher(dirName);
                    if (match.matches()) {
                        FileUtils.deleteDir(dir);
                    }
                }

                File usrDir = new File(targetPath);
                File[] usrDirs = usrDir.listFiles();
                String usrDirName = "";
                for (File dir : usrDirs) {
                    if (!dir.isDirectory()) {
                        continue;
                    }

                    usrDirName = dir.getName();
                    // 进入时间秒数目录
                    Matcher match = numberPattern.matcher(usrDirName);
                    if (match.matches()) {
                        FileUtils.deleteDir(dir);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Package p = this.getClass().getPackage();
        String version = p.getImplementationVersion();
        log.info("版本：" + version);
        LogMsg logMsg = new LogMsg("NJ版本：" + version);
        TcpServer.logList.add(logMsg);
//                tcpServer.exportData(1600068943, 1600424751, "");
    }

    /**
     *
     */
    private void initProperties() {
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream("zenb.conf");
            pro.load(in);
            in.close();

            devicePort = Integer.parseInt(pro.getProperty("devicePort", "6666"));
//                        deviceIp = pro.getProperty("deviceIP", "192.168.1.1.");
            listenPort = Integer.parseInt(pro.getProperty("listenPort", "6666"));

            dataPath = pro.getProperty("dataPath", "D:\\workspace\\ZENB\\Demo\\ZENB\\data04_OK/data/192.168.1.1/a13/");
            targetPath = pro.getProperty("targetPath", "D:\\workspace\\ZENB\\Demo\\ZENB\\data04_OK/usr/192.168.1.1/a13/");

//                        dataPath = pro.getProperty("dataPath", "/data/192.168.1.1/a13/");
//                        targetPath = pro.getProperty("targetPath", "/usr/192.168.1.1/a13/");
            checkInterval = Integer.parseInt(pro.getProperty("checkInterval", "100"));

            deleteDirOnStart = Boolean.parseBoolean(pro.getProperty("deleteDirOnStart", "false"));
            terminalAllow = pro.getProperty("terminal");

            String dzDevIp = pro.getProperty("dzDevIp");
            if (StrUtil.isNotEmpty(dzDevIp)) {
                DataCenter.devIpS = dzDevIp.split(",");
            }
            if(pro.containsKey("lteBandCount")){
                DataCenter.lteDevCount = Integer.parseInt(pro.getProperty("lteBandCount"));
            }
            System.err.println("zenb.conf.dbIp=" + pro.getProperty("dbIp"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        if (main == null) {
            main = new MainPro();
        }
    }

    /**
     * 清空data目录/data/192.168.1.1/a13/
     * 包括下面的毫秒目录（各种业务bcp目录），voip目录
     */
    public static void deleteDataDir() {
        File baseDir = new File(dataPath);
        File[] dirs = baseDir.listFiles();
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }

            FileUtils.deleteDir(dir);
        }
    }

    /**
     * 清空usr目录/usr/192.168.1.1/a13/
     * 包括下面的毫秒目录（各种业务bcp目录），voip目录
     */
    public static void deleteUsrDir() {
        File baseDir = new File(targetPath);
        File[] dirs = baseDir.listFiles();
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }

            FileUtils.deleteDir(dir);
        }
    }

    /**
     * 根据时间段删除data目录/data/192.168.1.1/a13/下面的毫秒目录（各种业务bcp目录）
     */
    public static void deleteDataDir(int startTime, int endTime) {
        File baseDir = new File(dataPath);
        File[] dirs = baseDir.listFiles();
        String dirName = "";
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }
            dirName = dir.getName();
            // 进入时间秒数目录
            Matcher match = numberPattern.matcher(dirName);
            if (match.matches()) {
                int dirIntTime = Integer.parseInt(dirName);
                if (dirIntTime > startTime && dirIntTime < endTime) {
                    FileUtils.deleteDir(dir);
                }
            }
        }
    }

    /**
     * 根据时间段删除usr目录/usr/192.168.1.1/a13/下面的毫秒目录（各种业务bcp目录）
     */
    public static void deleteUsrDir(int startTime, int endTime) {
        File baseDir = new File(targetPath);
        File[] dirs = baseDir.listFiles();
        String dirName = "";
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }
            dirName = dir.getName();
            // 进入时间秒数目录
            Matcher match = numberPattern.matcher(dirName);
            if (match.matches()) {
                int dirIntTime = Integer.parseInt(dirName);
                if (dirIntTime > startTime && dirIntTime < endTime) {
                    FileUtils.deleteDir(dir);
                }
            }
        }
    }

    /**
     * 根据voip的文件删除voip的语音目录，直接删除该文件的上层hours目录
     */
    public static void deleteVoipDataDir(List<String> voipEntityFileNames) {
        if (null != voipEntityFileNames && voipEntityFileNames.size() > 0) {
            File baseDir = new File(voipCapPath);

            for (String voipEntityFileName : voipEntityFileNames) {
                File[] dirs = baseDir.listFiles();
                String dirName = "";
                // 小时目录hours436590
                for (File dir : dirs) {
                    if (!dir.isDirectory()) {
                        continue;
                    }
                    dirName = dir.getName();
                    // voipEntityFileName形如/data//192.168.1.1/a13/voip/hour435054/volte/156619741800200014/Avolte_20190818235018_00014_000001.pcap
                    // 直接判断pcap文件名里是否有该小时目录名即可确定是否删除该目录
                    if (voipEntityFileName.indexOf(dirName) >= 0) {
                        FileUtils.deleteDir(dir);
                    }
//        				boolean hasEntityFile = hasEntityFile(voipEntityFileName, dir);
//        				if(hasEntityFile) {
//        					FileUtils.deleteDir(dir);
//        				}
                }
            }
        }
    }

    /**
     * 查找文件夹里是否存在该voipCap文件
     * @param entityFileName
     * @param dirFile
     * @return
     */
    private static boolean hasEntityFile(String entityFileName, File dirFile) {
        if (dirFile.isDirectory()) {
            File[] dirs = dirFile.listFiles();
            for (File temp : dirs) {
                hasEntityFile(entityFileName, temp);
            }
            if (dirs.length == 0) {
                return false;
            }
        } else {
            String dirName = dirFile.getName();
            if (dirName.indexOf(entityFileName) >= 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 删除voip的语音文件目录
     */
    public static void deleteVoipDataDir() {
        File baseDir = new File(voipCapPath);
        File[] dirs = baseDir.listFiles();
        for (File dir : dirs) {
            if (!dir.isDirectory()) {
                continue;
            }

            FileUtils.deleteDir(dir);
        }
    }

    public void runLinuxScript(String dateTimeStr) {
        try {
//				String[] command = new String[]{"sudo", "/work/ZenbDataCounter/setDate.sh", dateTimeStr};
//				Process proc = Runtime.getRuntime().exec("sudo date -s \"" + dateTimeStr + "\"");
            String[] command = new String[]{"/bin/sh", "-c", "date -s \"" + dateTimeStr + "\""};
            Process proc = Runtime.getRuntime().exec(command);
            System.err.println("runLinuxScript[" + dateTimeStr + "]");
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String text = null;
            //输出操作结果
            while ((text = br.readLine()) != null) {
                System.err.println(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TcpServer getTcpServer() {
        return tcpServer;
    }


}
