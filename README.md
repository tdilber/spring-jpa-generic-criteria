# JPA - EXTENSION

TODOs:

- All Tests
- Convert tumple to Object (done)
- Extend JpaRepository (done)
- Spring Boot Service For Data Fetch (done)
- Linq For Java (done)
- A Repository For Linq Support (done)
- Parantesis Support (done)
- not just inner join support, all joins support

customerRepository.query() <br />
.select(Select("user.name", "name"), Select("user.age"), Select("name", "surname"), Select("birthdate" , "
birthdate"))<br />
.distinct(false)<br />
.where(Parantesis(Field("id").eq(3), OR, Field("user.id").eq(4), OR, Field("id").eq(5)), Parantesis(Field("id").eq(6),
OR, Field("id").eq(4), OR, Field("user.id").eq(5)))<br />
.orderBy(OrderBy("user.name", Order.ASC))<br />
.page(0, 5)<br />
.getResult(User.class);<br />

Next Version Things

- Web Authorization Support.
- Web Rest Parameter Resolver.
