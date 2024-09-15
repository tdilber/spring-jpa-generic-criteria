package com.beyt.jdq.presetation;

import com.beyt.jdq.BaseTestInstance;
import com.beyt.jdq.TestApplication;
import com.beyt.jdq.annotation.model.JdqField;
import com.beyt.jdq.annotation.model.JdqModel;
import com.beyt.jdq.dto.Criteria;
import com.beyt.jdq.dto.CriteriaList;
import com.beyt.jdq.dto.DynamicQuery;
import com.beyt.jdq.dto.enums.CriteriaOperator;
import com.beyt.jdq.testenv.entity.authorization.AdminUser;
import com.beyt.jdq.testenv.repository.AdminUserRepository;
import com.beyt.jdq.util.PresentationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class S8_Advenced_Projection extends BaseTestInstance {
    private @Autowired AdminUserRepository adminUserRepository;


    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorizationSummary {
        @Getter @Setter private Long adminId;
        @Getter @Setter private String adminUsername;
        @Getter @Setter private Long roleId;
        @Getter @Setter private String roleName;
        @Getter @Setter private Long authorizationId;
        @Getter @Setter private String authorizationName;
        @Getter @Setter private String menuIcon;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AuthorizationSummary that)) return false;
            return Objects.equals(adminId, that.adminId) && Objects.equals(adminUsername, that.adminUsername) && Objects.equals(roleId, that.roleId) && Objects.equals(roleName, that.roleName) && Objects.equals(authorizationId, that.authorizationId) && Objects.equals(authorizationName, that.authorizationName) && Objects.equals(menuIcon, that.menuIcon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(adminId, adminUsername, roleId, roleName, authorizationId, authorizationName, menuIcon);
        }
    }
    @Test
    public void roleJoin() {
        DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.getSelect().add(Pair.of("id", "adminId"));
        dynamicQuery.getSelect().add(Pair.of("username", "adminUsername"));
        dynamicQuery.getSelect().add(Pair.of("roles.id", "roleId"));
        dynamicQuery.getSelect().add(Pair.of("roles.name", "roleName"));
        dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.id", "authorizationId"));
        dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.name", "authorizationName"));
        dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.menuIcon", "menuIcon"));
        var criteriaList = CriteriaList.of(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
        dynamicQuery.getWhere().addAll(criteriaList);
        PresentationUtil.prettyPrint(dynamicQuery);
        List<AdminUser> result = adminUserRepository.findAll(criteriaList);
        PresentationUtil.prettyPrint(result);
        assertEquals(List.of(adminUser1, adminUser2, adminUser3, adminUser4, adminUser5), result);

        List<AuthorizationSummary> result2 = adminUserRepository.findAll(dynamicQuery, AuthorizationSummary.class);
        PresentationUtil.prettyPrint(result2);

        assertEquals(List.of(new AuthorizationSummary(1L, "admin1", 1L, "role1", 1L, "auth1", "icon1"),
                new AuthorizationSummary(2L, "admin2", 2L, "role2", 2L, "auth2", "icon2"),
                new AuthorizationSummary(3L, "admin3", 3L, "role3", 3L, "auth3", "icon3"),
                new AuthorizationSummary(4L, "admin4", 4L, "role4", 4L, "auth4", "icon4"),
                new AuthorizationSummary(5L, "admin5", 5L, "role5", 5L, "auth5", "icon5")), result2);
    }


    @JdqModel // DONT MISS THIS ANNOTATION
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnotatedAuthorizationSummary {
        @JdqField("id")
        @Getter @Setter
        private Long adminId;
        @JdqField("username")
        @Getter @Setter private String adminUsername;
        @JdqField("roles.id")
        @Getter @Setter private Long roleId;
        @JdqField("roles.name")
        @Getter @Setter private String roleName;
        @JdqField("roles.roleAuthorizations.authorization.id")
        @Getter @Setter private Long authorizationId;
        @JdqField("roles.roleAuthorizations.authorization.name")
        @Getter @Setter private String authorizationName;
        @JdqField("roles.roleAuthorizations.authorization.menuIcon")
        @Getter @Setter private String menuIcon;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnnotatedAuthorizationSummary that)) return false;
            return Objects.equals(adminId, that.adminId) && Objects.equals(adminUsername, that.adminUsername) && Objects.equals(roleId, that.roleId) && Objects.equals(roleName, that.roleName) && Objects.equals(authorizationId, that.authorizationId) && Objects.equals(authorizationName, that.authorizationName) && Objects.equals(menuIcon, that.menuIcon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(adminId, adminUsername, roleId, roleName, authorizationId, authorizationName, menuIcon);
        }
    }

    @Test
    public void roleJoinWithAnnotatedModel() {
        DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.getWhere().add(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
        PresentationUtil.prettyPrint(dynamicQuery);
        List<AnnotatedAuthorizationSummary> result2 = adminUserRepository.findAll(dynamicQuery, AnnotatedAuthorizationSummary.class);
        PresentationUtil.prettyPrint(result2);

        assertEquals(List.of(new AnnotatedAuthorizationSummary(1L, "admin1", 1L, "role1", 1L, "auth1", "icon1"),
                new AnnotatedAuthorizationSummary(2L, "admin2", 2L, "role2", 2L, "auth2", "icon2"),
                new AnnotatedAuthorizationSummary(3L, "admin3", 3L, "role3", 3L, "auth3", "icon3"),
                new AnnotatedAuthorizationSummary(4L, "admin4", 4L, "role4", 4L, "auth4", "icon4"),
                new AnnotatedAuthorizationSummary(5L, "admin5", 5L, "role5", 5L, "auth5", "icon5")), result2);
    }
}
