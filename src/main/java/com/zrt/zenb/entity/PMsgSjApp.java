package com.zrt.zenb.entity;

import java.io.Serializable;

public class PMsgSjApp extends MsgBase implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.OnlyID
     *
     * @mbg.generated
     */
    private String onlyid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.DeviceID
     *
     * @mbg.generated
     */
    private String deviceid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.PID
     *
     * @mbg.generated
     */
    private Integer pid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.ClientIP
     *
     * @mbg.generated
     */
    private String clientip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.ClientPort
     *
     * @mbg.generated
     */
    private Integer clientport;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.ServerIP
     *
     * @mbg.generated
     */
    private String serverip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.ServerPort
     *
     * @mbg.generated
     */
    private Integer serverport;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.timestamp
     *
     * @mbg.generated
     */
    private Integer timestamp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.EntityFileName
     *
     * @mbg.generated
     */
    private String entityfilename;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strIMEI
     *
     * @mbg.generated
     */
    private String strimei;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strMAC
     *
     * @mbg.generated
     */
    private String strmac;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strImsi
     *
     * @mbg.generated
     */
    private String strimsi;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strAppName
     *
     * @mbg.generated
     */
    private String strappname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strDisplayname
     *
     * @mbg.generated
     */
    private String strdisplayname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.strAppVer
     *
     * @mbg.generated
     */
    private String strappver;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.szMainFileName
     *
     * @mbg.generated
     */
    private String szmainfilename;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjapp.nOPType
     *
     * @mbg.generated
     */
    private Integer noptype;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.id
     *
     * @return the value of t_sjapp.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.id
     *
     * @param id the value for t_sjapp.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.OnlyID
     *
     * @return the value of t_sjapp.OnlyID
     *
     * @mbg.generated
     */
    public String getOnlyid() {
        return onlyid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.OnlyID
     *
     * @param onlyid the value for t_sjapp.OnlyID
     *
     * @mbg.generated
     */
    public void setOnlyid(String onlyid) {
        this.onlyid = onlyid == null ? null : onlyid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.DeviceID
     *
     * @return the value of t_sjapp.DeviceID
     *
     * @mbg.generated
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.DeviceID
     *
     * @param deviceid the value for t_sjapp.DeviceID
     *
     * @mbg.generated
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.PID
     *
     * @return the value of t_sjapp.PID
     *
     * @mbg.generated
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.PID
     *
     * @param pid the value for t_sjapp.PID
     *
     * @mbg.generated
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.ClientIP
     *
     * @return the value of t_sjapp.ClientIP
     *
     * @mbg.generated
     */
    public String getClientip() {
        return clientip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.ClientIP
     *
     * @param clientip the value for t_sjapp.ClientIP
     *
     * @mbg.generated
     */
    public void setClientip(String clientip) {
        this.clientip = clientip == null ? null : clientip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.ClientPort
     *
     * @return the value of t_sjapp.ClientPort
     *
     * @mbg.generated
     */
    public Integer getClientport() {
        return clientport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.ClientPort
     *
     * @param clientport the value for t_sjapp.ClientPort
     *
     * @mbg.generated
     */
    public void setClientport(Integer clientport) {
        this.clientport = clientport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.ServerIP
     *
     * @return the value of t_sjapp.ServerIP
     *
     * @mbg.generated
     */
    public String getServerip() {
        return serverip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.ServerIP
     *
     * @param serverip the value for t_sjapp.ServerIP
     *
     * @mbg.generated
     */
    public void setServerip(String serverip) {
        this.serverip = serverip == null ? null : serverip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.ServerPort
     *
     * @return the value of t_sjapp.ServerPort
     *
     * @mbg.generated
     */
    public Integer getServerport() {
        return serverport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.ServerPort
     *
     * @param serverport the value for t_sjapp.ServerPort
     *
     * @mbg.generated
     */
    public void setServerport(Integer serverport) {
        this.serverport = serverport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.timestamp
     *
     * @return the value of t_sjapp.timestamp
     *
     * @mbg.generated
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.timestamp
     *
     * @param timestamp the value for t_sjapp.timestamp
     *
     * @mbg.generated
     */
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.EntityFileName
     *
     * @return the value of t_sjapp.EntityFileName
     *
     * @mbg.generated
     */
    public String getEntityfilename() {
        return entityfilename;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.EntityFileName
     *
     * @param entityfilename the value for t_sjapp.EntityFileName
     *
     * @mbg.generated
     */
    public void setEntityfilename(String entityfilename) {
        this.entityfilename = entityfilename == null ? null : entityfilename.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strIMEI
     *
     * @return the value of t_sjapp.strIMEI
     *
     * @mbg.generated
     */
    public String getStrimei() {
        return strimei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strIMEI
     *
     * @param strimei the value for t_sjapp.strIMEI
     *
     * @mbg.generated
     */
    public void setStrimei(String strimei) {
        this.strimei = strimei == null ? null : strimei.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strMAC
     *
     * @return the value of t_sjapp.strMAC
     *
     * @mbg.generated
     */
    public String getStrmac() {
        return strmac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strMAC
     *
     * @param strmac the value for t_sjapp.strMAC
     *
     * @mbg.generated
     */
    public void setStrmac(String strmac) {
        this.strmac = strmac == null ? null : strmac.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strImsi
     *
     * @return the value of t_sjapp.strImsi
     *
     * @mbg.generated
     */
    public String getStrimsi() {
        return strimsi;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strImsi
     *
     * @param strimsi the value for t_sjapp.strImsi
     *
     * @mbg.generated
     */
    public void setStrimsi(String strimsi) {
        this.strimsi = strimsi == null ? null : strimsi.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strAppName
     *
     * @return the value of t_sjapp.strAppName
     *
     * @mbg.generated
     */
    public String getStrappname() {
        return strappname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strAppName
     *
     * @param strappname the value for t_sjapp.strAppName
     *
     * @mbg.generated
     */
    public void setStrappname(String strappname) {
        this.strappname = strappname == null ? null : strappname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strDisplayname
     *
     * @return the value of t_sjapp.strDisplayname
     *
     * @mbg.generated
     */
    public String getStrdisplayname() {
        return strdisplayname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strDisplayname
     *
     * @param strdisplayname the value for t_sjapp.strDisplayname
     *
     * @mbg.generated
     */
    public void setStrdisplayname(String strdisplayname) {
        this.strdisplayname = strdisplayname == null ? null : strdisplayname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.strAppVer
     *
     * @return the value of t_sjapp.strAppVer
     *
     * @mbg.generated
     */
    public String getStrappver() {
        return strappver;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.strAppVer
     *
     * @param strappver the value for t_sjapp.strAppVer
     *
     * @mbg.generated
     */
    public void setStrappver(String strappver) {
        this.strappver = strappver == null ? null : strappver.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.szMainFileName
     *
     * @return the value of t_sjapp.szMainFileName
     *
     * @mbg.generated
     */
    public String getSzmainfilename() {
        return szmainfilename;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.szMainFileName
     *
     * @param szmainfilename the value for t_sjapp.szMainFileName
     *
     * @mbg.generated
     */
    public void setSzmainfilename(String szmainfilename) {
        this.szmainfilename = szmainfilename == null ? null : szmainfilename.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjapp.nOPType
     *
     * @return the value of t_sjapp.nOPType
     *
     * @mbg.generated
     */
    public Integer getNoptype() {
        return noptype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjapp.nOPType
     *
     * @param noptype the value for t_sjapp.nOPType
     *
     * @mbg.generated
     */
    public void setNoptype(Integer noptype) {
        this.noptype = noptype;
    }
    
    public static final int fileContentsLen = 41;
    
    public PMsgSjApp() {
    	
	}
    
    public PMsgSjApp(String[] fileContents) {
    	int index = 0;

		onlyid = fileContents[index ++];
		deviceid = fileContents[index ++];
		index ++;
		try {
			pid = Integer.parseInt(fileContents[index ++]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		clientip = fileContents[index ++];
		try {
			clientport = Integer.parseInt(fileContents[index ++]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		index ++;
		serverip = fileContents[index ++];
		try {
			serverport = Integer.parseInt(fileContents[index ++]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		index ++;
		index ++;
		try {
			timestamp = Integer.parseInt(fileContents[index ++]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		index += 21;
		entityfilename = fileContents[index ++];
		strimei = fileContents[index ++];
		strmac = fileContents[index ++];
		strimsi = fileContents[index ++];
		strappname = fileContents[index ++];
		strdisplayname = fileContents[index ++];
		strappver = fileContents[index ++];
//		szmainfilename = fileContents[index ++];
//		try {
//			noptype = Integer.parseInt(fileContents[index ++]);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
    }
}