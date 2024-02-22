# JPA - EXTENSION

This project look like QueryDsl but main purpuse is query creation with simple DTO (Pojo) objects. So we can simply
ingrate with frontend application filters or somethings. Received query converted to Jpa Specification object and this
Specification<Entity> executed on JpaSpecificationExecutor<Entity> repository.

```java
  
   // Entity
  @Entity
  @Table(name = "customer")
  public class Customer implements Serializable {

      private static final long serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(name = "name")
      private String name;

      @Column(name = "age")
      private Integer age;

      @Column(name = "birthdate")
      private Instant birthdate;

      @ManyToOne
      private User user;
  
      // getter - setters 
  }
  
  // Repository Creation
  @Repository
  public interface CustomerRepository extends JpaExtendedRepository<Customer, Long> {
  }
  
   // Usage
   // SELECT * FROM customer WHERE age >= 18
   customerRepository.findAllWithCriteria(Arrays.asList(Criteria.of("age", CriteriaType.GREATER_THAN_OR_EQUAL, 18)));
   
   // Default AND operation apply on list of all criterias.
   // SELECT * FROM customer WHERE age >= 18 AND id < 10
   customerRepository.findAllWithCriteria(Arrays.asList(
                                 Criteria.of("age", CriteriaType.GREATER_THAN_OR_EQUAL, 18),
                                 Criteria.of("id", CriteriaType.LESS_THAN, 10)
                                 ));
                                                        
  // OR Usage 
  // SELECT * FROM customer WHERE age >= 18 OR id < 10
  customerRepository.findAllWithCriteria(Arrays.asList(
                                 Criteria.of("age", CriteriaType.GREATER_THAN_OR_EQUAL, 18),
                                 Criteria.of("", CriteriaType.OR),
                                 Criteria.of("id", CriteriaType.LESS_THAN, 10)
                                 ));
                                                        
  // Joinning Usage
  // SELECT c.* FROM customer c INNER JOIN user u ON c.user_id=u.id WHERE u.surname LIKE '%son%'
   customerRepository.findAllWithCriteria(Arrays.asList(Criteria.of("user.surname", CriteriaType.CONTAIN, "son")));
                                                      
  // SELECT c.* FROM customer c LEFT JOIN user u ON c.user_id=u.id WHERE u.surname LIKE '%son%'
   customerRepository.findAllWithCriteria(Arrays.asList(Criteria.of("user<surname", CriteriaType.CONTAIN, "son")));
 
  // SELECT c.* FROM customer c RIGHT JOIN user u ON c.user_id=u.id WHERE u.surname LIKE '%son%'
   customerRepository.findAllWithCriteria(Arrays.asList(Criteria.of("user>surname", CriteriaType.CONTAIN, "son")));
                                                      
                                                      
  // Powerfull Usage
  // SELECT c.* FROM customer c INNER JOIN user u ON c.user_id=u.id WHERE (u.surname LIKE '%son%' AND c.age >= 18) OR (u.name LIKE '%ander%' AND c.id < 10)
   customerRepository.findAllWithCriteria(Arrays.asList(
                                                      Criteria.of("user.surname", CriteriaType.CONTAIN, "son"),
                                                      Criteria.of("age", CriteriaType.GREATER_THAN_OR_EQUAL, 18),
                                                      Criteria.of("", CriteriaType.OR),
                                                      Criteria.of("user.name", CriteriaType.CONTAIN, "ander"),
                                                      Criteria.of("id", CriteriaType.LESS_THAN, 10)
                                                      ));
```
