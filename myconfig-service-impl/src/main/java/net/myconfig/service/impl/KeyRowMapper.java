package net.myconfig.service.impl;

import static net.myconfig.service.db.SQLColumns.DESCRIPTION;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.TYPEID;
import static net.myconfig.service.db.SQLColumns.TYPEPARAM;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.myconfig.core.model.Key;
import net.myconfig.core.type.ValueType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class KeyRowMapper implements RowMapper<Key> {
	@Override
	public Key mapRow(ResultSet rs, int i) throws SQLException {
		String typeId = rs.getString(TYPEID);
		if (StringUtils.isBlank(typeId)) {
			typeId = ValueType.PLAIN;
		}
		return new Key(rs.getString(NAME), rs.getString(DESCRIPTION), typeId, rs.getString(TYPEPARAM));
	}
}