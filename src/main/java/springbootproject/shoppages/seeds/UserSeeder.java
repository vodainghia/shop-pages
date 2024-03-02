package springbootproject.shoppages.seeds;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springbootproject.shoppages.contracts.repositories.RoleRepositoryInterface;
import springbootproject.shoppages.contracts.repositories.UserRepositoryInterface;
import springbootproject.shoppages.enums.Roles;
import springbootproject.shoppages.helpers.Common;
import springbootproject.shoppages.models.Role;
import springbootproject.shoppages.models.User;

import java.util.List;

@Component
public class UserSeeder {
    private static final Logger LOGGER = LogManager.getLogger(UserSeeder.class);
    private final UserRepositoryInterface userRepo;
    private final JdbcTemplate jdbcTemplate;
    private final RoleRepositoryInterface roleRepo;

    @Autowired
    public UserSeeder(JdbcTemplate jdbcTemplate, UserRepositoryInterface userRepositoryInterface, RoleRepositoryInterface roleRepositoryInterface) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepo = userRepositoryInterface;
        this.roleRepo = roleRepositoryInterface;
    }

    @EventListener
    @Transactional
    public void seeds(ContextRefreshedEvent event) {
        if (Common.isTableEmpty(userRepo)) {
            seedUsersTable();
        }
    }

    private void seedUsersTable() {
        String sql = "SELECT email FROM users U WHERE U.email = \"admin@nomail.com\" LIMIT 1";
        List<Object> admin = this.jdbcTemplate.query(sql, (resultSet, rowNum) -> null);

        if (admin.isEmpty()) {
            User user = new User();
            user.setName("Nghia QA");
            user.setEmail("admin@nomail.com");
            user.setPassword("12345");

            Role adminRole = new Role();
            adminRole.setName(Roles.ROLE_ADMIN.toString());
            this.roleRepo.save(adminRole);
            user.setRoles(List.of(adminRole));
            this.userRepo.save(user);

            LOGGER.info("User Admin \"admin@nomail.com\" has been seeded");
        } else {
            LOGGER.trace("User Seeding Not Required");
        }
    }
}
