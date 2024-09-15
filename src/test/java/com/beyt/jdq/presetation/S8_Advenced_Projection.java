package com.beyt.jdq.presetation;

import com.beyt.jdq.BaseTestInstance;
import com.beyt.jdq.TestApplication;
import com.beyt.jdq.annotation.model.JdqField;
import com.beyt.jdq.annotation.model.JdqModel;
import com.beyt.jdq.annotation.model.JdqSubModel;
import com.beyt.jdq.dto.Criteria;
import com.beyt.jdq.dto.CriteriaList;
import com.beyt.jdq.dto.DynamicQuery;
import com.beyt.jdq.dto.enums.CriteriaOperator;
import com.beyt.jdq.testenv.entity.authorization.AdminUser;
import com.beyt.jdq.testenv.repository.AdminUserRepository;
import com.beyt.jdq.util.PresentationUtil;
import lombok.*;
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
    @EqualsAndHashCode
    public static class AuthorizationSummary {
        @Getter
        @Setter
        private Long adminId;
        @Getter
        @Setter
        private String adminUsername;
        @Getter
        @Setter
        private Long roleId;
        @Getter
        @Setter
        private String roleName;
        @Getter
        @Setter
        private Long authorizationId;
        @Getter
        @Setter
        private String authorizationName;
        @Getter
        @Setter
        private String menuIcon;

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
    @EqualsAndHashCode
    public static class AnnotatedAuthorizationSummary {
        @JdqField("id")
        @Getter
        @Setter
        private Long adminId;
        @JdqField("username")
        @Getter
        @Setter
        private String adminUsername;
        @JdqField("roles.id")
        @Getter
        @Setter
        private Long roleId;
        @JdqField("roles.name")
        @Getter
        @Setter
        private String roleName;
        @JdqField("roles.roleAuthorizations.authorization.id")
        @Getter
        @Setter
        private Long authorizationId;
        @JdqField("roles.roleAuthorizations.authorization.name")
        @Getter
        @Setter
        private String authorizationName;
        @JdqField("roles.roleAuthorizations.authorization.menuIcon")
        @Getter
        @Setter
        private String menuIcon;

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


    @JdqModel
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class AnnotatedAuthorizationSummarySubModel {
        @JdqField("id")
        @Getter
        @Setter
        private Long adminId;
        @JdqField("username")
        @Getter
        @Setter
        private String adminUsername;

        @JdqSubModel("roles")
        private Role role;

        @JdqModel
        public record Role(
                @JdqField("id") Long roleId,
                @JdqField("name") String roleName,
                @JdqSubModel("roleAuthorizations") RoleAuthorization roleAuthorization) {


            @JdqModel
            public record RoleAuthorization(
                    @JdqField("authorization.id") Long authorizationId,
                    @JdqField("authorization.name") String authorizationName,
                    @JdqField("authorization.menuIcon") String menuIcon
            ) {

            }
        }
    }


    @Test
    public void roleJoinAnnotatedAuthorizationSummarySubModel() {
        DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.getWhere().add(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
        PresentationUtil.prettyPrint(dynamicQuery);
        List<AnnotatedAuthorizationSummarySubModel> result2 = adminUserRepository.findAll(dynamicQuery, AnnotatedAuthorizationSummarySubModel.class);
        PresentationUtil.prettyPrint(result2);

        assertEquals(List.of(new AnnotatedAuthorizationSummarySubModel(1L, "admin1", new AnnotatedAuthorizationSummarySubModel.Role(1L, "role1", new AnnotatedAuthorizationSummarySubModel.Role.RoleAuthorization(1L, "auth1", "icon1"))),
                new AnnotatedAuthorizationSummarySubModel(2L, "admin2", new AnnotatedAuthorizationSummarySubModel.Role(2L, "role2", new AnnotatedAuthorizationSummarySubModel.Role.RoleAuthorization(2L, "auth2", "icon2"))),
                new AnnotatedAuthorizationSummarySubModel(3L, "admin3", new AnnotatedAuthorizationSummarySubModel.Role(3L, "role3", new AnnotatedAuthorizationSummarySubModel.Role.RoleAuthorization(3L, "auth3", "icon3"))),
                new AnnotatedAuthorizationSummarySubModel(4L, "admin4", new AnnotatedAuthorizationSummarySubModel.Role(4L, "role4", new AnnotatedAuthorizationSummarySubModel.Role.RoleAuthorization(4L, "auth4", "icon4"))),
                new AnnotatedAuthorizationSummarySubModel(5L, "admin5", new AnnotatedAuthorizationSummarySubModel.Role(5L, "role5", new AnnotatedAuthorizationSummarySubModel.Role.RoleAuthorization(5L, "auth5", "icon5")))), result2);
    }


    @JdqModel
    public record AnnotatedAuthorizationSummarySubModel2(
            @JdqField("id") Long adminId,
            @JdqField("username") String adminUsername,
            @JdqSubModel("roles") Role2 role) {

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @JdqModel
        @EqualsAndHashCode
        public static class Role2 {
            private @JdqField("id") Long roleId;
            private @JdqField("name") String roleName;
            private @JdqSubModel("roleAuthorizations") RoleAuthorization2 roleAuthorization;


            @JdqModel
            public record RoleAuthorization2(
                    @JdqField("authorization.id") Long authorizationId,
                    @JdqField("authorization.name") String authorizationName,
                    @JdqField("authorization.menuIcon") String menuIcon
            ) {
            }
        }
    }

    @Test
    public void roleJoinAnnotatedAuthorizationSummarySubModel2() {
        DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.getWhere().add(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
        PresentationUtil.prettyPrint(dynamicQuery);
        List<AnnotatedAuthorizationSummarySubModel2> result2 = adminUserRepository.findAll(dynamicQuery, AnnotatedAuthorizationSummarySubModel2.class);
        PresentationUtil.prettyPrint(result2);

        assertEquals(List.of(new AnnotatedAuthorizationSummarySubModel2(1L, "admin1", new AnnotatedAuthorizationSummarySubModel2.Role2(1L, "role1", new AnnotatedAuthorizationSummarySubModel2.Role2.RoleAuthorization2(1L, "auth1", "icon1"))),
                new AnnotatedAuthorizationSummarySubModel2(2L, "admin2", new AnnotatedAuthorizationSummarySubModel2.Role2(2L, "role2", new AnnotatedAuthorizationSummarySubModel2.Role2.RoleAuthorization2(2L, "auth2", "icon2"))),
                new AnnotatedAuthorizationSummarySubModel2(3L, "admin3", new AnnotatedAuthorizationSummarySubModel2.Role2(3L, "role3", new AnnotatedAuthorizationSummarySubModel2.Role2.RoleAuthorization2(3L, "auth3", "icon3"))),
                new AnnotatedAuthorizationSummarySubModel2(4L, "admin4", new AnnotatedAuthorizationSummarySubModel2.Role2(4L, "role4", new AnnotatedAuthorizationSummarySubModel2.Role2.RoleAuthorization2(4L, "auth4", "icon4"))),
                new AnnotatedAuthorizationSummarySubModel2(5L, "admin5", new AnnotatedAuthorizationSummarySubModel2.Role2(5L, "role5", new AnnotatedAuthorizationSummarySubModel2.Role2.RoleAuthorization2(5L, "auth5", "icon5")))), result2);
    }


    @JdqModel
    public record AnnotatedAuthorizationSummarySubModel3(
            @JdqField("id") Long adminId,
            @JdqField("username") String adminUsername,
            @JdqSubModel() Role3 role) { // empty sub model annotation

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @JdqModel
        @EqualsAndHashCode
        public static class Role3 {
            private @JdqField("roles.id") Long roleId;
            private @JdqField("roles.name") String roleName;
            private @JdqSubModel("roles.roleAuthorizations") RoleAuthorization3 roleAuthorization;


            @JdqModel
            public record RoleAuthorization3(
                    @JdqField("authorization.id") Long authorizationId,
                    @JdqField("authorization.name") String authorizationName,
                    @JdqField("authorization.menuIcon") String menuIcon
            ) {
            }
        }
    }

    @Test
    public void roleJoinAnnotatedAuthorizationSummarySubModel3EmptySubModel() {
        DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.getWhere().add(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
        PresentationUtil.prettyPrint(dynamicQuery);
        List<AnnotatedAuthorizationSummarySubModel3> result2 = adminUserRepository.findAll(dynamicQuery, AnnotatedAuthorizationSummarySubModel3.class);
        PresentationUtil.prettyPrint(result2);

        assertEquals(List.of(new AnnotatedAuthorizationSummarySubModel3(1L, "admin1", new AnnotatedAuthorizationSummarySubModel3.Role3(1L, "role1", new AnnotatedAuthorizationSummarySubModel3.Role3.RoleAuthorization3(1L, "auth1", "icon1"))),
                new AnnotatedAuthorizationSummarySubModel3(2L, "admin2", new AnnotatedAuthorizationSummarySubModel3.Role3(2L, "role2", new AnnotatedAuthorizationSummarySubModel3.Role3.RoleAuthorization3(2L, "auth2", "icon2"))),
                new AnnotatedAuthorizationSummarySubModel3(3L, "admin3", new AnnotatedAuthorizationSummarySubModel3.Role3(3L, "role3", new AnnotatedAuthorizationSummarySubModel3.Role3.RoleAuthorization3(3L, "auth3", "icon3"))),
                new AnnotatedAuthorizationSummarySubModel3(4L, "admin4", new AnnotatedAuthorizationSummarySubModel3.Role3(4L, "role4", new AnnotatedAuthorizationSummarySubModel3.Role3.RoleAuthorization3(4L, "auth4", "icon4"))),
                new AnnotatedAuthorizationSummarySubModel3(5L, "admin5", new AnnotatedAuthorizationSummarySubModel3.Role3(5L, "role5", new AnnotatedAuthorizationSummarySubModel3.Role3.RoleAuthorization3(5L, "auth5", "icon5")))), result2);
    }
}
