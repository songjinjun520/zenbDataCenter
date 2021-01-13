package com.zrt.zenb.entity.enums;

/**
 * 运营商类型
 * @author Administrator
 *
 */
public enum SpType {
	/**
	 * 5 移动4G
	 */
	PLMN_CM_4G,
	/**
	 * 6 联通4G
	 */
	PLMN_CU_4G,
	/**
	 * 7 电信4G
	 */
	PLMN_TE_4G;

	public static String getSpTypeDesc(SpType spType){
		if(spType == PLMN_CM_4G){
			return "移动4G";
		}
		else if(spType == PLMN_CU_4G){
			return "联通4G";
		}
		else if(spType == PLMN_TE_4G){
			return "电信4G";
		}
		else {
			return "移动4G";
		}
	}

	public static SpType getSpType(int spTypeValue){
		for(SpType spType : SpType.values()){
			if(spType.ordinal() == spTypeValue){
				return spType;
			}
		}
		return PLMN_CM_4G;
	}

	public String toString() {
		return getSpTypeDesc(this);
	}

}
