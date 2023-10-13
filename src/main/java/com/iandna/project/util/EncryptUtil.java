package com.iandna.project.util;

import com.iandna.project.config.model.MapData;

import java.math.BigInteger;
import java.security.MessageDigest;

public interface EncryptUtil {
	public static String getSHA256(String input) {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	public static String getSHA512(String input) {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	public static String getBntzEncryptCd(String custId, String cpnCd, String timeSession) {
		String encryptCd = getSHA512(custId + cpnCd + timeSession).toLowerCase();
		return encryptCd;
	}
	
	public static String getBntzToken(String timeSession) {
		String hashKey = "iandna77";
		return getSHA256(hashKey + timeSession);
	}
	
	public static boolean checkCafe24EncryptCd(MapData param) {
		String checkCd = param.getString("encrypt_cd");
		boolean passFlag = false;
		
		String timeSession = DateUtil.getCurrentDateTime12();
		String timeSession2 = DateUtil.getBefTenMinDateTime12();
		timeSession = timeSession.substring(2, 9);
		timeSession2 = timeSession2.substring(2, 9);
		String encryptCd = null;
		String encryptCd2 = null;
		if(param.getString("frcsCustNo").contains("@")) {
			encryptCd = getSHA512(param.getString("shopId") + "icaanfden2a4" + timeSession).toLowerCase();
			encryptCd2 = getSHA512(param.getString("shopId") + "icaanfden2a4" + timeSession2).toLowerCase();	
		}else {
			encryptCd = getSHA512(param.getString("frcsCustNo") + "icaanfden2a4" + timeSession).toLowerCase();
			encryptCd2 = getSHA512(param.getString("frcsCustNo") + "icaanfden2a4" + timeSession2).toLowerCase();
		}
		
		
		if(checkCd.equals(encryptCd) || checkCd.equals(encryptCd2)) {
			passFlag = true;
		}
		return passFlag;
	}
	
	public static void main(String[] args) {
		System.out.println(getBntzEncryptCd("ehej_cn@nav@s", "P0000DBB", "0512151"));
//		System.out.println(getSHA512("weleda!"));
//		
//		MapData param = new MapData();
//		param.set("frcsCustNo", "test1116");
//		param.set("encrypt_cd", "c9f88006834f746a137579a3004bfc2d149cce21170df19c4e6ed0811bc331e267f443f7ca661dfd567a953b98f61e448707636d402303c6b3e361f3584c2fdd");
//		
//		System.out.println(checkCafe24EncryptCd(param));
	}
}