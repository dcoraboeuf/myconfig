package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.myconfig.core.model.Key;
import net.myconfig.test.AbstractIntegrationTest;
import net.myconfig.test.DBUnitHelper;

import org.junit.Test;

public class KeyRowMapperTest extends AbstractIntegrationTest {

	private final KeyRowMapper mapper = new KeyRowMapper();

	@Test
	public void type_plain_as_null() throws SQLException {
		Key key = loadKey("text");
		assertNotNull(key);
		assertEquals("text", key.getName());
		assertEquals("Some text", key.getDescription());
		assertEquals("plain", key.getTypeId());
		assertNull(key.getTypeParam());
	}

	@Test
	public void type_plain() throws SQLException {
		Key key = loadKey("text2");
		assertNotNull(key);
		assertEquals("text2", key.getName());
		assertEquals("Some other text", key.getDescription());
		assertEquals("plain", key.getTypeId());
		assertNull(key.getTypeParam());
	}

	@Test
	public void type_boolean() throws SQLException {
		Key key = loadKey("bool");
		assertNotNull(key);
		assertEquals("bool", key.getName());
		assertEquals("Some boolean", key.getDescription());
		assertEquals("boolean", key.getTypeId());
		assertNull(key.getTypeParam());
	}

	@Test
	public void type_regex() throws SQLException {
		Key key = loadKey("regex");
		assertNotNull(key);
		assertEquals("regex", key.getName());
		assertEquals("Some regular expression", key.getDescription());
		assertEquals("regex", key.getTypeId());
		assertEquals("\\d+", key.getTypeParam());
	}

	protected Key loadKey(String name) throws SQLException {
		Key key;
		Connection c = DBUnitHelper.getConnection().getConnection();
		PreparedStatement ps = c.prepareStatement(String.format("select * from appkey where name = '%s'", name));
		try {
			ResultSet rs = ps.executeQuery();
			try {
				if (rs.next()) {
					key = mapper.mapRow(rs, 1);
				} else {
					throw new IllegalStateException("Could not find record");
				}
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}
		return key;
	}

}
