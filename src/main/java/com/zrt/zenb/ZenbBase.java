/**
 *
 */
package com.zrt.zenb;

/**
 * @author PGW
 *
 */
public abstract class ZenbBase {

        /**
         *
         */
        protected ZenbBase() {
        }


//        protected static String deviceIp;

        protected static int devicePort;

        protected static int listenPort;

        protected static String dataPath;

        protected static String targetPath;

        protected static String voipCapPath = "/data/192.168.1.1/a13/voip";
        
        protected static int checkInterval;
        
        /**
         * 设备启动时删除毫秒目录（/data/192.168.1.1/a13/下面的毫秒目录）
         */
        protected static boolean deleteDirOnStart = false;

        protected static String terminalAllow = "";
        
        public static String getTerminalAllow() {
        	return terminalAllow;
        }

}
