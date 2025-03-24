package org.example.stablecoinchecker.chart.infra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamedLockWithJdbcTemplate {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    private final DataSource dataSource;

    public void executeWithNamedLock(
            final String userLockName,
            final int timeoutSeconds,
            final Runnable runnable
    ) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                getLock(userLockName, timeoutSeconds, conn);

                runnable.run();
            } finally {
                releaseLock(userLockName, conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void getLock(final String userLockName, final int timeoutSeconds, final Connection conn)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(GET_LOCK)) {
            stmt.setString(1, userLockName);
            stmt.setInt(2, timeoutSeconds);

            checkResult(stmt);
        }
    }

    private void releaseLock(final String userLockName, final Connection conn)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(RELEASE_LOCK)) {
            stmt.setString(1, userLockName);

            checkResult(stmt);
        }
    }

    private void checkResult(final PreparedStatement stmt) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (!resultSet.next()) {
                throw new RuntimeException("Named Lock 쿼리 결과 값이 없습니다.");
            }
            int result = resultSet.getInt(1);
            if (result != 1) {
                throw new RuntimeException("Named Lock 쿼리 결과 값이 1이 아닙니다.");
            }
        }
    }
}
