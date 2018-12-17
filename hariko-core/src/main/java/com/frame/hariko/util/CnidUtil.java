package com.frame.hariko.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public final class CnidUtil {
	private static final String REGPEX_18_BIT = "\\d{17}([0-9]|X)";
	private static final String REGPEX_15_BIT = "\\d{15}";

	private static final String BIRTHDAY_18_BIT = "^((19|20)\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
	private static final String BIRTHDAY_15_BIT = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";

	private static final char[] CHINA_ID_CARD_VALIDATE_BIT = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

	private static final int CNID_LENGTH_15 = 15; // 15位长度身份证
	private static final int CNID_LENGTH_18 = 18; // 18位长度身份证

	public enum Gender {
		FEMALE, MALE, UNKNOWN
	}

	/**
	 * 验证身份证号码是否符合要求
	 *
	 * @param idCard
	 * @return
	 */
	public static boolean isValidCnid(String idCard) {
		if (!isValidLength(idCard)) {
			return false;
		}

		int idLen = idCard.length();
		boolean result = false;
		result = isValidFormat(idCard) && isValidBirthday(idCard);
		if (idLen == CNID_LENGTH_18 && result) {
			result = result && isValidLastBit(idCard);
		}
		return result;
	}

	// check the date if it is valid
	private static boolean checkDate(String birthday) {
		if (birthday == null) {
			return false;
		}

		String syear = "1900";
		String smonth = "01";
		String sday = "01";
		if (birthday.length() == 6) {
			syear = "19" + birthday.substring(0, 2);
			smonth = birthday.substring(2, 4);
			sday = birthday.substring(4, 6);
		} else if (birthday.length() == 8) {
			syear = birthday.substring(0, 4);
			smonth = birthday.substring(4, 6);
			sday = birthday.substring(6, 8);
		} else {
			return false;
		}
		int year = Integer.parseInt(syear);
		int month = Integer.parseInt(smonth.substring(0, 1).equals("0") ? smonth.substring(1, 2) : smonth);
		int day = Integer.parseInt(sday.substring(0, 1).equals("0") ? sday.substring(1, 2) : sday);
		boolean result = false;
		if (year >= 1900 && year < Calendar.getInstance().get(Calendar.YEAR)) {
			if (month >= 1 && month <= 12) {
				if (day >= 1 && day <= getDaysOfMonth(year, month)) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * check the format
	 *
	 * @param idCard
	 * @return
	 */
	private static boolean isValidFormat(String idCard) {
		return Pattern.matches(REGPEX_18_BIT, idCard) || Pattern.matches(REGPEX_15_BIT, idCard);
	}

	/**
	 * 校验18位身份证最后一位是否正确
	 *
	 * @param idCard
	 * @return
	 */
	private static boolean isValidLastBit(String idCard) {
		if (StringUtils.isEmpty(idCard) || idCard.length() != CNID_LENGTH_18) {
			return false;
		}
		char validateBit = idCard.charAt(idCard.length() - 1);
		int validateVale = getValidateValue(idCard);
		return CHINA_ID_CARD_VALIDATE_BIT[validateVale] == validateBit;
	}

	private static int[] getEveryBitWeightValue() {
		int[] weightValueArray = new int[18];
		for (int i = 18, j = 0; i >= 1; i--, j++) {
			weightValueArray[j] = (int) (Math.pow(2, i - 1) % 11);
		}
		return weightValueArray;
	}

	private static int getValidateValue(String idcard) {
		char[] bits = idcard.toCharArray();
		int result = 0;
		int[] weigthValueArray = getEveryBitWeightValue();
		for (int i = 0, j = idcard.length() - 1; i < j; i++) {
			result += Integer.parseInt(bits[i] + "") * weigthValueArray[i];
		}
		return result % 11;
	}

	// validate birthday
	private static boolean isValidBirthday(String idcard) {
		if (!isValidLength(idcard)) {
			return false;
		}

		int bitSize = idcard.length();
		boolean result = false;
		if (bitSize == CNID_LENGTH_15) {
			String birthday = idcard.substring(6, 12);
			if (Pattern.matches(BIRTHDAY_15_BIT, birthday)) {
				result = checkDate(birthday);
			}
		} else if (bitSize == CNID_LENGTH_18) {
			String birthday = idcard.substring(6, 14);
			if (Pattern.matches(BIRTHDAY_18_BIT, birthday)) {
				result = checkDate(birthday);
			}
		}
		return result;
	}

	private static int getDaysOfMonth(int iYear, int iMnonth) {
		Calendar cal = new GregorianCalendar(iYear, iMnonth - 1, 1);
		// Get the number of days in that month
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 检查身份证的长度是否合法
	 *
	 * @param cnid
	 * @return
	 */
	private static boolean isValidLength(String cnid) {
		if (StringUtils.isEmpty(cnid)) {
			return false;
		}
		int len = cnid.length();
		if (len == CNID_LENGTH_15 || len == CNID_LENGTH_18) {
			return true;
		}
		return false;
	}

	/**
	 * 从身份证中获取性别
	 *
	 * @param cnid
	 * @return
	 */
	public static Gender getGenderFromCnid(String cnid) {
		if (!isValidCnid(cnid)) {
			return Gender.UNKNOWN;
		}
		if (StringUtils.isNotBlank(cnid)) {
			int len = cnid.length();
			switch (len) {
			case CNID_LENGTH_18:
				return isNumEven(cnid.charAt(len - 2)) ? Gender.FEMALE : Gender.MALE;
			case CNID_LENGTH_15:
				return isNumEven(cnid.charAt(len - 1)) ? Gender.FEMALE : Gender.MALE;
			default:
				break;
			}
		}
		return Gender.UNKNOWN;
	}

	private static boolean isNumEven(int num) {
		return (num & 1) == 0;
	}

	/**
	 * 从身份证中获取年龄
	 *
	 * @param cnid
	 * @return
	 */
	public static Integer getAgeFromCnid(String cnid) {
		if (!isValidCnid(cnid)) {
			throw new IllegalArgumentException("身份证不合法:[" + cnid + "]");
		}
		Date birthday = getBirthdayWithCnid(cnid);
		if (birthday != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int yearCur = cal.get(Calendar.YEAR);
			int monthCur = cal.get(Calendar.MONTH);
			int dayCur = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBir = cal.get(Calendar.YEAR);
			int monthBir = cal.get(Calendar.MONTH);
			int dayBir = cal.get(Calendar.DAY_OF_MONTH);

			return (yearCur - yearBir - ((monthCur > monthBir) || (monthCur == monthBir && dayCur >= dayBir) ? 0 : 1));
		}

		return null;
	}

	private static Date getBirthdayWithCnid(String cnid) {
		String birthdayStr = null;
		switch (cnid.length()) {
		case CNID_LENGTH_18:
			birthdayStr = cnid.substring(6, 14);
			break;

		case CNID_LENGTH_15:
			birthdayStr = "19" + cnid.substring(6, 12);
			break;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDD");
		try {
			return dateFormat.parse(birthdayStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("身份证号码不合法:[" + cnid + "];");
		}
	}

}
