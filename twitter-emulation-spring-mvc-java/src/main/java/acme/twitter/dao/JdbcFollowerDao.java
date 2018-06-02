package acme.twitter.dao;

import acme.twitter.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of follower DAO.
 */
@Repository
public class JdbcFollowerDao implements FollowerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcFollowerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int countFollowingByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "select count(*) " +
                        "from account a, follower f " +
                        "where a.account_id = f.who_account_id " +
                        "  and a.username = ?",
                new Object[]{username},
                Integer.class);
    }

    @Override
    public int countFollowersByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "select count(*) " +
                        "from account a, follower f " +
                        "where a.account_id = f.whom_account_id " +
                        "  and a.username = ?",
                new Object[]{username},
                Integer.class);
    }

    @Override
    public boolean isFollow(String whoUsername, String whomUsername) {
        int count = jdbcTemplate.queryForObject(
                "select count(*) " +
                        "from account a1, account a2, follower f " +
                        "where a1.account_id = f.who_account_id " +
                        "  and a2.account_id = f.whom_account_id " +
                        "  and a1.username = ? " +
                        "  and a2.username = ?",
                new Object[]{whoUsername, whomUsername},
                Integer.class);

        return (count > 0);
    }

    @Override
    public void follow(Account whoAccount, Account whomAccount) {
        jdbcTemplate.update(
                "insert into follower (who_account_id, whom_account_id) " +
                        "select ?, ? " +
                        "where not exists ( " +
                        "   select * from follower " +
                        "      where who_account_id = ? " +
                        "        and whom_account_id = ?)",
                whoAccount.getId(), whomAccount.getId(),
                whoAccount.getId(), whomAccount.getId());
    }

    @Override
    public void unfollow(Account whoAccount, Account whomAccount) {
        jdbcTemplate.update(
                "delete from follower " +
                        "where who_account_id = ? " +
                        "  and whom_account_id = ?",
                whoAccount.getId(), whomAccount.getId());
    }
}