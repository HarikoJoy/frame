package com.frame.hariko.mybatis.deprecated.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTypeHandler extends BaseTypeHandler<Date>
{
	/*
	 * For time zone GMT+8, it should be 8
	 */
	private static final int HOURS_OFFSET = calculateUtcOffsetInHours();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException
	{
		ps.setTimestamp(i, new Timestamp(offsetDate(parameter, -HOURS_OFFSET).getTime()));
	}

	@Override
	public Date getNullableResult(ResultSet rs, String columnName) throws SQLException
	{
		Timestamp sqlTimestamp = rs.getTimestamp(columnName);
		if (sqlTimestamp != null)
		{
			return offsetDate(sqlTimestamp, HOURS_OFFSET);
		}
		return null;
	}

	@Override
	public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException
	{
		Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
		if (sqlTimestamp != null)
		{
			return offsetDate(sqlTimestamp, HOURS_OFFSET);
		}
		return null;
	}

	@Override
	public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
	{
		Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
		if (sqlTimestamp != null)
		{
			return offsetDate(sqlTimestamp, HOURS_OFFSET);
		}
		return null;
	}

	private Date offsetDate(Date sqlTimestamp, int hoursOffset)
	{
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		date.setTimeInMillis(sqlTimestamp.getTime());
		date.add(Calendar.HOUR_OF_DAY, hoursOffset);
		return date.getTime();
	}


	/**
	 *
	 * @return
     */
	private static int calculateUtcOffsetInHours()
	{
		int timeZoneHoursInMills = TimeZone.getTimeZone(ZoneId.systemDefault()).getRawOffset()
				- TimeZone.getTimeZone(ZoneOffset.UTC).getRawOffset();
		long oneHoursInMills = ChronoUnit.HOURS.getDuration().toMillis();

		return (int) (timeZoneHoursInMills / oneHoursInMills);
	}

}
