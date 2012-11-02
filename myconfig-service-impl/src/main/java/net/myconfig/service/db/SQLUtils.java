package net.myconfig.service.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class SQLUtils {

	private SQLUtils() {
	}

	public static Timestamp now() {
		return new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis());
	}

	public static DateTime getDateTime(ResultSet rs, String columnName) throws SQLException {
		Timestamp timestamp = rs.getTimestamp(columnName);
		return getDateTime(timestamp);
	}

	public static DateTime getDateTime(Timestamp timestamp) {
		return new DateTime(timestamp.getTime(), DateTimeZone.UTC);
	}

	public static <E extends Enum<E>> E getEnum(Class<E> enumClass, ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if (value == null) {
			return null;
		} else {
			return Enum.valueOf(enumClass, value);
		}
	}

}
