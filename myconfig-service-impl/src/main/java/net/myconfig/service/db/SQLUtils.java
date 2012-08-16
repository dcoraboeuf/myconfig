package net.myconfig.service.db;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class SQLUtils {

	private SQLUtils() {
	}

	public static Timestamp now() {
		return new Timestamp(DateTime.now(DateTimeZone.UTC).getMillis());
	}

}
