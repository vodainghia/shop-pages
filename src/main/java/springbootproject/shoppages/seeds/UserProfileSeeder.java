package springbootproject.shoppages.seeds;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springbootproject.shoppages.contracts.repositories.RoleRepositoryInterface;
import springbootproject.shoppages.contracts.repositories.UserRepositoryInterface;
import springbootproject.shoppages.helpers.Common;
import springbootproject.shoppages.models.Role;
import springbootproject.shoppages.models.User;

import java.util.List;

@Component
public class UserProfileSeeder {
    private static final Logger LOGGER = LogManager.getLogger(UserProfileSeeder.class);
    private final UserRepositoryInterface userRepo;
    private final JdbcTemplate jdbcTemplate;
    private final RoleRepositoryInterface roleRepo;

    @Autowired
    public UserProfileSeeder(JdbcTemplate jdbcTemplate, UserRepositoryInterface userRepositoryInterface, RoleRepositoryInterface roleRepositoryInterface) {
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
        String sql = "SELECT email FROM users U WHERE U.email = \"admin@mail.com\" LIMIT 1";
        List<Object> admin = this.jdbcTemplate.query(sql, (resultSet, rowNum) -> null);

        if (admin.isEmpty()) {
            User user = new User();
            user.setName("Nghia QA");
            user.setEmail("admin@nomail.com");
            user.setPassword(new BCryptPasswordEncoder().encode("12345"));

            Role role = new springbootproject.shoppages.models.Role();
            role.setName(springbootproject.shoppages.enums.Roles.ROLE_ADMIN.toString());

            this.roleRepo.save(role);

            user.setRoles(List.of(role));

            this.userRepo.save(user);

            LOGGER.info("Users Seeded");
        } else {
            LOGGER.trace("Users Seeding Not Required");
        }
    }

    private void seedCustomersTable() {
        User user = new User();
        user.setName(Common.generateRandomChars(10, null));
        user.setEmail(Common.generateRandomChars(10, null) + "@mail.com");
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));

        Role role = setRoleUser();

        user.setRoles(List.of(role));

        this.userRepo.save(user);

        LOGGER.info("Customers Seeded");
    }

    private Role setRoleUser() {
        Role role = this.roleRepo.findByName(springbootproject.shoppages.enums.Roles.ROLE_USER.toString());

        if (role == null) {
            Role newRole = new Role();
            newRole.setName(springbootproject.shoppages.enums.Roles.ROLE_USER.toString());
            this.roleRepo.save(newRole);

            return newRole;
        }

        return role;
    }
}
