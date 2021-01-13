package com.zrt.zenb.entity;

public class PMsgSjInfo extends MsgBase {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.OnlyID
     *
     * @mbg.generated
     */
    private String onlyid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.DeviceID
     *
     * @mbg.generated
     */
    private String deviceid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.PID
     *
     * @mbg.generated
     */
    private Integer pid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.ClientIP
     *
     * @mbg.generated
     */
    private String clientip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.ClientPort
     *
     * @mbg.generated
     */
    private Integer clientport;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.ServerIP
     *
     * @mbg.generated
     */
    private String serverip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.ServerPort
     *
     * @mbg.generated
     */
    private Integer serverport;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.timestamp
     *
     * @mbg.generated
     */
    private Integer timestamp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.EntityFileName
     *
     * @mbg.generated
     */
    private String entityfilename;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.realmac
     *
     * @mbg.generated
     */
    private String realmac;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strMAC
     *
     * @mbg.generated
     */
    private String strmac;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strIMEI
     *
     * @mbg.generated
     */
    private String strimei;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strIMSI
     *
     * @mbg.generated
     */
    private String strimsi;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strMSISDN
     *
     * @mbg.generated
     */
    private String strmsisdn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strModel
     *
     * @mbg.generated
     */
    private String strmodel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.strUserAgent
     *
     * @mbg.generated
     */
    private String struseragent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.nMobInfoFromApp
     *
     * @mbg.generated
     */
    private Integer nmobinfofromapp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sjinfo.RES0
     *
     * @mbg.generated
     */
    private String res0;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.id
     *
     * @return the value of t_sjinfo.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.id
     *
     * @param id the value for t_sjinfo.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.OnlyID
     *
     * @return the value of t_sjinfo.OnlyID
     *
     * @mbg.generated
     */
    public String getOnlyid() {
        return onlyid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.OnlyID
     *
     * @param onlyid the value for t_sjinfo.OnlyID
     *
     * @mbg.generated
     */
    public void setOnlyid(String onlyid) {
        this.onlyid = onlyid == null ? "" : onlyid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.DeviceID
     *
     * @return the value of t_sjinfo.DeviceID
     *
     * @mbg.generated
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.DeviceID
     *
     * @param deviceid the value for t_sjinfo.DeviceID
     *
     * @mbg.generated
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? "" : deviceid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.PID
     *
     * @return the value of t_sjinfo.PID
     *
     * @mbg.generated
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.PID
     *
     * @param pid the value for t_sjinfo.PID
     *
     * @mbg.generated
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.ClientIP
     *
     * @return the value of t_sjinfo.ClientIP
     *
     * @mbg.generated
     */
    public String getClientip() {
        return clientip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.ClientIP
     *
     * @param clientip the value for t_sjinfo.ClientIP
     *
     * @mbg.generated
     */
    public void setClientip(String clientip) {
        this.clientip = clientip == null ? "" : clientip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.ClientPort
     *
     * @return the value of t_sjinfo.ClientPort
     *
     * @mbg.generated
     */
    public Integer getClientport() {
        return clientport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.ClientPort
     *
     * @param clientport the value for t_sjinfo.ClientPort
     *
     * @mbg.generated
     */
    public void setClientport(Integer clientport) {
        this.clientport = clientport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.ServerIP
     *
     * @return the value of t_sjinfo.ServerIP
     *
     * @mbg.generated
     */
    public String getServerip() {
        return serverip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.ServerIP
     *
     * @param serverip the value for t_sjinfo.ServerIP
     *
     * @mbg.generated
     */
    public void setServerip(String serverip) {
        this.serverip = serverip == null ? "" : serverip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.ServerPort
     *
     * @return the value of t_sjinfo.ServerPort
     *
     * @mbg.generated
     */
    public Integer getServerport() {
        return serverport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.ServerPort
     *
     * @param serverport the value for t_sjinfo.ServerPort
     *
     * @mbg.generated
     */
    public void setServerport(Integer serverport) {
        this.serverport = serverport;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.timestamp
     *
     * @return the value of t_sjinfo.timestamp
     *
     * @mbg.generated
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.timestamp
     *
     * @param timestamp the value for t_sjinfo.timestamp
     *
     * @mbg.generated
     */
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.EntityFileName
     *
     * @return the value of t_sjinfo.EntityFileName
     *
     * @mbg.generated
     */
    public String getEntityfilename() {
        return entityfilename;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.EntityFileName
     *
     * @param entityfilename the value for t_sjinfo.EntityFileName
     *
     * @mbg.generated
     */
    public void setEntityfilename(String entityfilename) {
        this.entityfilename = entityfilename == "" ? null : entityfilename.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.realmac
     *
     * @return the value of t_sjinfo.realmac
     *
     * @mbg.generated
     */
    public String getRealmac() {
        return realmac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.realmac
     *
     * @param realmac the value for t_sjinfo.realmac
     *
     * @mbg.generated
     */
    public void setRealmac(String realmac) {
        this.realmac = realmac == null ? "" : realmac.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strMAC
     *
     * @return the value of t_sjinfo.strMAC
     *
     * @mbg.generated
     */
    public String getStrmac() {
        return strmac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strMAC
     *
     * @param strmac the value for t_sjinfo.strMAC
     *
     * @mbg.generated
     */
    public void setStrmac(String strmac) {
        this.strmac = strmac == null ? "" : strmac.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strIMEI
     *
     * @return the value of t_sjinfo.strIMEI
     *
     * @mbg.generated
     */
    public String getStrimei() {
        return strimei;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strIMEI
     *
     * @param strimei the value for t_sjinfo.strIMEI
     *
     * @mbg.generated
     */
    public void setStrimei(String strimei) {
        this.strimei = strimei == null ? "" : strimei.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strIMSI
     *
     * @return the value of t_sjinfo.strIMSI
     *
     * @mbg.generated
     */
    public String getStrimsi() {
        return strimsi;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strIMSI
     *
     * @param strimsi the value for t_sjinfo.strIMSI
     *
     * @mbg.generated
     */
    public void setStrimsi(String strimsi) {
        this.strimsi = strimsi == null ? "" : strimsi.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strMSISDN
     *
     * @return the value of t_sjinfo.strMSISDN
     *
     * @mbg.generated
     */
    public String getStrmsisdn() {
        return strmsisdn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strMSISDN
     *
     * @param strmsisdn the value for t_sjinfo.strMSISDN
     *
     * @mbg.generated
     */
    public void setStrmsisdn(String strmsisdn) {
        this.strmsisdn = strmsisdn == null ? "" : strmsisdn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strModel
     *
     * @return the value of t_sjinfo.strModel
     *
     * @mbg.generated
     */
    public String getStrmodel() {
        return strmodel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strModel
     *
     * @param strmodel the value for t_sjinfo.strModel
     *
     * @mbg.generated
     */
    public void setStrmodel(String strmodel) {
        this.strmodel = strmodel == null ? "" : strmodel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.strUserAgent
     *
     * @return the value of t_sjinfo.strUserAgent
     *
     * @mbg.generated
     */
    public String getStruseragent() {
        return struseragent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.strUserAgent
     *
     * @param struseragent the value for t_sjinfo.strUserAgent
     *
     * @mbg.generated
     */
    public void setStruseragent(String struseragent) {
        this.struseragent = struseragent == "" ? "" : struseragent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.nMobInfoFromApp
     *
     * @return the value of t_sjinfo.nMobInfoFromApp
     *
     * @mbg.generated
     */
    public Integer getNmobinfofromapp() {
        return nmobinfofromapp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.nMobInfoFromApp
     *
     * @param nmobinfofromapp the value for t_sjinfo.nMobInfoFromApp
     *
     * @mbg.generated
     */
    public void setNmobinfofromapp(Integer nmobinfofromapp) {
        this.nmobinfofromapp = nmobinfofromapp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sjinfo.RES0
     *
     * @return the value of t_sjinfo.RES0
     *
     * @mbg.generated
     */
    public String getRes0() {
        return res0;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sjinfo.RES0
     *
     * @param res0 the value for t_sjinfo.RES0
     *
     * @mbg.generated
     */
    public void setRes0(String res0) {
        this.res0 = res0 == null ? "" : res0.trim();
    }
    
    public static final int fileContentsLen = 42;
    
    public PMsgSjInfo() {
    	
    }
    
    public PMsgSjInfo(String[] fileContents) {
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
		realmac = fileContents[index ++];
		strmac = fileContents[index ++];
		strimei = fileContents[index ++];
		strimsi = fileContents[index ++];
		strmsisdn = fileContents[index ++];
		strmodel = fileContents[index ++];
		struseragent = fileContents[index ++];
		try {
			nmobinfofromapp = Integer.parseInt(fileContents[index ++]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
}