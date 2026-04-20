package com.ewaste.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ItemJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertEwasteItems(Long pickupId, List<Map<String, Object>> itemsList) {
        String sql = "INSERT INTO ewaste_items (pickup_id, item_type, weight) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> item = itemsList.get(i);
                ps.setLong(1, pickupId);
                ps.setString(2, (String) item.get("itemType"));
                ps.setDouble(3, (Double) item.get("weight"));
            }

            @Override
            public int getBatchSize() {
                return itemsList.size();
            }
        });
    }
}
