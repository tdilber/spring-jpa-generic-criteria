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

### 1- Setting up the project with Maven

```maven
<dependency>
    <groupId>io.github.tdilber</groupId>
    <artifactId>spring-jpa-dynamic-query</artifactId>
    <version>0.3.1</version>
</dependency>
```

### 2- Enable Annotation

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

### 3- Create a Repository

**Create a Repository for Existing Entity.**

```java
 public interface AdminUserRepository extends JpaDynamicQueryRepository<AdminUser, Long> {

}
```

### 4- Operator Examples

##### What is Criteria?
At the beginning we must understand what is Criteria. Criteria is SQL Query WHERE Clause item. 

For example `SELECT * FROM user WHERE id > 5 AND name like 'Ali%' AND surname = 'DILBER' AND age IN (29, 30, 31) `  
- `id > 5` is a Criteria => `Criteria.of("id", CriteriaOperator.GREATER_THAN, 5)`
- `name like 'Ali%'` is a Criteria => `Criteria.of("name", CriteriaOperator.START_WITH, "Ali")`
- `surname = 'DILBER'` is a Criteria => `Criteria.of("name", CriteriaOperator.EQUAL, "DILBER")`
- `age IN (29, 30, 31)` is a Criteria => `Criteria.of("age", CriteriaOperator.EQUAL, 29, 30, 31)`

this is it :)


#####  Multi Value Supported Criteria Operators

Some operators have **multi value support**. This means that you can use multiple values for the same field.
* Multi Value **support** is available for the following operators: _EQUAL, NOT_EQUAL, CONTAIN, DOES_NOT_CONTAIN, START_WITH_
* Multi Value **not supported** for the following operators: _SPECIFIED, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, END_WITH_

**Note:** As you know Sql Where Clause Some operators have multi value input, for example **IN, NOT IN**. We develop more multi value operators with java code touches.

#### Comparable Operators

This operator is used to compare numbers and dates. Available Java Types are **Date, Double, Long, LocalDate, ZonedDateTime, Instant, Integer**.

The following operators are available:

`EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL`

Enums supported for `EQUAL, NOT_EQUAL` operators.


```java
userRepository.findAll(CriteriaList.of(Criteria.of("status", CriteriaOperator.EQUAL, User.Status.ACTIVE)));
customerRepository.findAll(CriteriaList.of(Criteria.of("age", CriteriaOperator.NOT_EQUAL, 23, 24, 25)));
```
_Hibernate Query:_
```sql
-- CriteriaOperator.EQUAL => User.Status.ACTIVE
select user0_.id        as id1_1_,
       user0_.age       as age2_1_,
       user0_.birthdate as birthdat3_1_,
       user0_.name      as name4_1_,
       user0_.status    as status5_1_,
       user0_.surname   as surname6_1_,
       user0_.type      as type7_1_
from test_user user0_
where user0_.status = ?

-- Multi Value CriteriaOperator.NOT_EQUAL => 23, 24, 25
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.age <> 23
  and customer0_.age <> 24
  and customer0_.age <> 25
```

#### String Operators

This operator is used to compare strings. The following operators are available:

`EQUAL, NOT_EQUAL, CONTAIN, DOES_NOT_CONTAIN, END_WITH, START_WITH`


```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.CONTAIN, "Customer")));
```
_Hibernate Query:_
```sql
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.name like ?
```

_Multi Value Support Examples:_

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.DOES_NOT_CONTAIN, "5", "4")));
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.START_WITH, "Customer 3", "Customer 4")));
```
_Hibernate Query:_
```sql
-- Multi Value CriteriaOperator.DOES_NOT_CONTAIN
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where (customer0_.name not like ?)
  and (customer0_.name not like ?)

-- Multi Value CriteriaOperator.START_WITH
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.name like ?
   or customer0_.name like ?
```

#### Null Check Operator

This operator is used to check if the field is null or not. The following operators are available: `SPECIFIED`

```java
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.SPECIFIED, true)));
customerRepository.findAll(CriteriaList.of(Criteria.of("name", CriteriaOperator.SPECIFIED, false)));
```
_Hibernate Query:_
```sql
-- CriteriaOperator.SPECIFIED true
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.name is not null

-- CriteriaOperator.SPECIFIED false
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.name is null
```


### 5- AND-OR Operator Examples

Sequentially, all criteria are evaluated with the AND operator. If you want to evaluate the criteria with the `OR`
operator, you can use the `Criteria.OR()` method.

```java
customerRepository.findAll(CriteriaList.of(
        Criteria.of("name", CriteriaOperator.EQUAL, "Customer 1"), 
        Criteria.OR(),
        Criteria.of("name", CriteriaOperator.EQUAL, "Customer 2")));

customerRepository.findAll(CriteriaList.of(
        Criteria.of("age", CriteriaOperator.EQUAL, 23, 24),
                        Criteria.of("age", CriteriaOperator.NOT_EQUAL, 20, 21),
                        Criteria.OR(), // ( [ (23 or 24) AND (not 20 and not 21) ] "OR" [ (not 24) AND (25 or 26) ])
                        Criteria.of("age", CriteriaOperator.NOT_EQUAL, 24),
                        Criteria.of("age", CriteriaOperator.EQUAL, 25, 26)));
```
_Hibernate Query:_
```sql
-- Criteria.OR() First Example
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where customer0_.name = ?
   or customer0_.name = ?

-- Criteria.OR() Second Example
select customer0_.id        as id1_0_,
       customer0_.age       as age2_0_,
       customer0_.birthdate as birthdat3_0_,
       customer0_.name      as name4_0_,
       customer0_.user_id   as user_id5_0_
from customer customer0_
where (customer0_.age = 23 or customer0_.age = 24) and customer0_.age <> 20 and customer0_.age <> 21
   or customer0_.age <> 24 and (customer0_.age = 25 or customer0_.age = 26)
```

### 6- SCOPE Operator Examples

Just And-Or operators are not enough for complex queries. For Example: you cannot this simple query with just AND-OR operators: `(A OR B) AND (C OR D)`.  For this reason, the `CriteriaOperator.PARENTHES` operator is used to create a scope.

```java
    //     (A OR B) AND (C OR D)
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
_Hibernate Query:_
```sql
select course0_.id                as id1_3_,
       course0_.active            as active2_3_,
       course0_.description       as descript3_3_,
       course0_.max_student_count as max_stud4_3_,
       course0_.name              as name5_3_,
       course0_.start_date        as start_da6_3_
from course course0_
where (course0_.id = 1 or course0_.id = 2)
  and (course0_.id = 2 or course0_.id = 3)
```

### 7- JOIN Examples 



### 8- Projection Examples

```java
DynamicQuery dynamicQuery = new DynamicQuery();
dynamicQuery.getSelect().add(Pair.of("id", "adminId"));
dynamicQuery.getSelect().add(Pair.of("username", "adminUsername"));
dynamicQuery.getSelect().add(Pair.of("roles.id", "roleId"));
dynamicQuery.getSelect().add(Pair.of("roles.name", "roleName"));
dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.id", "authorizationId"));
dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.name", "authorizationName"));
dynamicQuery.getSelect().add(Pair.of("roles.roleAuthorizations.authorization.menuIcon", "menuIcon"));
dynamicQuery.getWhere().addAll(criteriaList);
var criteriaList = CriteriaList.of(Criteria.of("roles.roleAuthorizations.authorization.menuIcon", CriteriaOperator.START_WITH, "icon"));

List<AuthorizationSummary> result2 = adminUserRepository.findAll(dynamicQuery, AuthorizationSummary.class);
```

### 9- Pagination Examples

```java
DynamicQuery dynamicQuery = new DynamicQuery();
dynamicQuery.setWhere(CriteriaList.of(Criteria.of(Course.Fields.id, CriteriaOperator.GREATER_THAN, 3)));
dynamicQuery.setPageSize(2);
dynamicQuery.setPageNumber(1);

Page<Course> result = courseRepository.findAllAsPage(dynamicQuery);
```

### 10- Query Builder Examples

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

### 11- Argument Resolver Examples

```java
```

### 12- Custom Converter
bu kutuphaneyi kullanmaya basladiginizda gercekten basiniza bela olacak seyler olacak bunlardan en onemlisi. 
### 13-  Custom Entity Manager Provider

### 14- Additional Features

## More Potential(Future) Features

## Performance

## Security

## Conclusion

Hicbir islem bir onceki islemi iptal etmiyor. Operatorlerle Join, join le Or-Scope, Or ve Scope ile Projectionu, Projection ile OrderBy, OrderBy ile Pageable gibi bir cok farkli islemi bir arada kullanabilirsiniz. Bunlarin hepsini Query Builder, Argment Resolver 
This introduction not enough pls visit https://github.com/tdilber/spring-jpa-dynamic-query-presentation-demo address for
more specific examples and details.



