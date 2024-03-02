# Spring Jpa Dynamic Query (JDQ)

This project is designed to overcome the sluggishness of Spring Data JPA's query creation and the need to write separate
code for each query. At its core, it simplifies the Criteria API introduced in Jpa 2, enabling programmatic or dynamic
runtime query creation.

The query creation capabilities include 9 different field operators, AND-OR conjunctions, SCOPE support, and single or
multi JOIN features. The SELECT, DISTINCT, ORDER BY clauses in the query are also supported with Joined Column. In
addition, all query results can be returned as both List<?> and Page<?>. After SELECT clause, PROJECTION support is also
available. This PROJECTION works with ignore missing fields rules for more flexibility.

You don't need to write any lines of code to create these queries, all operations including all JOIN operations are done
dynamically at runtime. It is sufficient to create just one Repository interface. The objects required to call the
methods related to dynamic query in the interface can be taken from within the program or from outside the program (like
DTO) as Serializable. Since all query creation and database query operations are done with Criteria Api during these
operations, it is as secure as Spring Data JPA, and works as fast and effectively as Spring Data JPA.

## Introduction

**This is the base Models for Jpa Dynamic Query.**

```java
public enum CriteriaOperator {
    //    String Operators
    CONTAIN, DOES_NOT_CONTAIN, END_WITH, START_WITH,
    //    Null Check Operator
    SPECIFIED,
    //    String and Comparable Operators
    EQUAL, NOT_EQUAL,
    //    Comparable Operators
    GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL,
    //    OR Operator
    OR,
    //    SCOPE Operator
    PARENTHES
}


@Getter
@Setter
public class Criteria implements Serializable {
    protected String key;
    protected CriteriaOperator operation;
    protected List<Object> values;
}

@Getter
@Setter
public class DynamicQuery implements Serializable {
    protected boolean distinct = false;
    protected Integer pageSize = null;
    protected Integer pageNumber = null;
    protected List<Pair<String, String>> select = new ArrayList<>();
    protected List<Criteria> where = new CriteriaList();
    protected List<Pair<String, Order>> orderBy = new ArrayList<>();
}
```

**This is the base Methods for JpaDynamicQueryRepository.**

```java

@NoRepositoryBean
public interface JpaDynamicQueryRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    // List Entity with WHERE Clause (JOIN supported)
    List<T> findAll(List<Criteria> criteriaList);

    // List Entity with WHERE Clause (JOIN supported) and Pageable
    Page<T> findAll(List<Criteria> criteriaList, Pageable pageable);

    // List Entity with SELECT, DISTINCT, WHERE, ORDER BY Clause (JOIN supported) Page Supported 
    List<T> findAll(DynamicQuery dynamicQuery);

    // List Entity with SELECT, DISTINCT, WHERE, ORDER BY Clause (JOIN supported) Page Supported
    Page<T> findAllAsPage(DynamicQuery dynamicQuery);

    // List Entity with SELECT, DISTINCT, WHERE, ORDER BY Clause (JOIN supported) Page Supported With Projection
    <ResultType> List<ResultType> findAll(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);

    // List Entity with SELECT, DISTINCT, WHERE, ORDER BY Clause (JOIN supported) Page Supported With Projection
    <ResultType> Page<ResultType> findAllAsPage(DynamicQuery dynamicQuery, Class<ResultType> resultTypeClass);
}
```

## Writing the Code

You can find the sample code from: https://github.com/tdilber/spring-jpa-dynamic-query-presentation-demo

### Setting up the project with Maven

```maven
<dependency>
    <groupId>io.github.tdilber</groupId>
    <artifactId>spring-jpa-dynamic-query</artifactId>
    <version>0.3.1</version>
</dependency>
```

### Enable Annotation

Add the `@EnableJpaDynamicQuery` annotation to the main class of your project. This annotation is used to enable the
dynamic query feature.

```java

@EnableJpaDynamicQuery
@SpringBootApplication
public class SpringJpaDynamicQueryDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringJpaDynamicQueryDemoApplication.class, args);
    }
}

```

### Create a Repository

**Create a Repository for Existing Entity.**

```java
 public interface AdminUserRepository extends JpaDynamicQueryRepository<AdminUser, Long> {

}
```

### Operator Examples

#### EQUAL Operator

```java
userRepository.findAll(CriteriaList.of(Criteria.of("status", CriteriaOperator.EQUAL, User.Status.ACTIVE)));
```

#### NOT_EQUAL Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.NOT_EQUAL, 24)));
```

#### GREATER_THAN Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.GREATER_THAN, 24)));
```

#### GREATER_THAN_OR_EQUAL Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.GREATER_THAN_OR_EQUAL, 24)));
```

#### LESS_THAN Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.LESS_THAN, 24)));
```

#### LESS_THAN_OR_EQUAL Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.LESS_THAN_OR_EQUAL, 24)));
```

#### CONTAIN Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.CONTAIN, "Customer")));
```

#### DOES_NOT_CONTAIN Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.DOES_NOT_CONTAIN, "5")));
```

#### START_WITH Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.START_WITH, "Customer")));
```

#### END_WITH Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.END_WITH, " 7")));
```

#### SPECIFIED Operator

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.SPECIFIED, true)));
```

### AND-OR Operator Examples

Sequentially, all criteria are evaluated with the AND operator. If you want to evaluate the criteria with the OR
operator, you can use the `Criteria.OR()` method.

```java
customerRepository.findAll(CriteriaList.of(
        Criteria.of("name", CriteriaOperator.EQUAL, "Customer 1"), 
    Criteria.

OR(),
    Criteria.

of("name",CriteriaOperator.EQUAL, "Customer 2")
));
```

### SCOPE Operator Examples

```java
var criteriaList = CriteriaList.of(
        Criteria.of("", CriteriaOperator.PARENTHES,
                CriteriaList.of(Criteria.of(Course.Fields.id, CriteriaOperator.EQUAL, 1),
                        Criteria.OR(),
                        Criteria.of(Course.Fields.id, CriteriaOperator.EQUAL, 2))),
        Criteria.of("", CriteriaOperator.PARENTHES,
                CriteriaList.of(Criteria.of(Course.Fields.id, CriteriaOperator.EQUAL, 2),
                        Criteria.OR(),
                        Criteria.of(Course.Fields.id, CriteriaOperator.EQUAL, 3)))
);
List<Course> courseList = courseRepository.findAll(criteriaList);
```

### Pagination Examples

```java
DynamicQuery dynamicQuery = new DynamicQuery();
dynamicQuery.

setWhere(CriteriaList.of(Criteria.of(Course.Fields.id, CriteriaOperator.GREATER_THAN, 3)));
        dynamicQuery.

setPageSize(2);
dynamicQuery.

setPageNumber(1);

Page<Course> result = courseRepository.findAllAsPage(dynamicQuery);
```

### Projection Examples

```java
DynamicQuery dynamicQuery = new DynamicQuery();
dynamicQuery.

getSelect().

add(Pair.of("id", "adminId"));
        dynamicQuery.

getSelect().

add(Pair.of("username", "adminUsername"));
        dynamicQuery.

getSelect().

add(Pair.of("roles.id", "roleId"));
        dynamicQuery.

getSelect().

add(Pair.of("roles.name", "roleName"));
        dynamicQuery.

getSelect().

add(Pair.of("roles.roleAuthorizations.authorization.id", "authorizationId"));
        dynamicQuery.

getSelect().

add(Pair.of("roles.roleAuthorizations.authorization.name", "authorizationName"));
        dynamicQuery.

getSelect().

add(Pair.of("roles.roleAuthorizations.authorization.menuIcon", "menuIcon"));
var criteriaList = CriteriaList.of(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));
dynamicQuery.

getWhere().

addAll(criteriaList);

List<AuthorizationSummary> result2 = adminUserRepository.findAll(dynamicQuery, AuthorizationSummary.class);
```

### Query Builder Examples

```java
Page<AuthorizationSummary> result = adminUserRepository.queryBuilder()
        .select(Select("id", "adminId"),
                Select("username", "adminUsername"),
                Select("roles.id", "roleId"),
                Select("roles.name", "roleName"),
                Select("roles.roleAuthorizations.authorization.id", "authorizationId"),
                Select("roles.roleAuthorizations.authorization.name", "authorizationName"),
                Select("roles.roleAuthorizations.authorization.menuIcon", "menuIcon"))
        .distinct(false)
        .where(Field("roles.roleAuthorizations.authorization.menuIcon").startWith("icon"), Parantesis(Field("id").eq(3), OR, Field("roles.id").eq(4), OR, Field("id").eq(5)), Parantesis(Field("id").eq(5), OR, Field("id").eq(4), OR, Field("roles.id").eq(3)))
        .orderBy(OrderBy("roles.id", Order.DESC))
        .page(1, 2)
        .getResultAsPage(AuthorizationSummary.class);
```

## Conclusion

This introduction not enough pls visit https://github.com/tdilber/spring-jpa-dynamic-query-presentation-demo address for
more specific examples and details.



