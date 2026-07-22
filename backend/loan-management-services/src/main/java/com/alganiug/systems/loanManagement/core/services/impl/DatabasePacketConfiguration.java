package com.alganiug.systems.loanManagement.core.services.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/** Ensures MySQL can persist documents up to the application's 10 MB upload limit. */
public class DatabasePacketConfiguration {

    private DataSource dataSource;
    private long maximumPacketBytes = 16777216L;

    public void initialize() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long current = jdbcTemplate.queryForObject("select @@global.max_allowed_packet", Long.class);
        if (current == null || current < maximumPacketBytes) {
            jdbcTemplate.execute("set global max_allowed_packet = " + maximumPacketBytes);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaximumPacketBytes(long maximumPacketBytes) {
        this.maximumPacketBytes = maximumPacketBytes;
    }
}