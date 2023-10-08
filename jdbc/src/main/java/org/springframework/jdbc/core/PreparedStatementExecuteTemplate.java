package org.springframework.jdbc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementExecuteTemplate {

    private static final Logger log = LoggerFactory.getLogger(PreparedStatementExecuteTemplate.class);

    private final DataSource dataSource;

    public PreparedStatementExecuteTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T execute(final PreparedStatementExecute<T> callBack,
                         final String sql,
                         final Object... args
    ) {
        final Connection conn = DataSourceUtils.getConnection(dataSource);

        return execute(conn, callBack, sql, args);
    }

    private <T> T execute(final Connection conn,
                          final PreparedStatementExecute<T> callBack,
                          final String sql,
                          final Object... args
    ) {
        try (final PreparedStatement pstmt = prepareMappedStatement(conn, sql, args);) {

            return callBack.callback(pstmt);
        } catch (SQLException e) {
            log.warn("쿼리 실행 도중에 오류가 발생하였습니다. {} ====> SQL = {} {} ====> Args = {}",
                    System.lineSeparator(), sql, System.lineSeparator(), args, e);
            throw new DataAccessException(e);
        }
    }

    private PreparedStatement prepareMappedStatement(final Connection conn, final String sql, final Object[] args) throws SQLException {
        final PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int argumentIndex = 1; argumentIndex <= args.length; argumentIndex++) {
            pstmt.setObject(argumentIndex, args[argumentIndex - 1]);
        }

        return pstmt;
    }
}
