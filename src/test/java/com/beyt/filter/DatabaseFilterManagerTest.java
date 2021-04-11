package com.beyt.filter;

import com.beyt.TestApplication;
import com.beyt.dto.Criteria;
import com.beyt.dto.CriteriaFilter;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.dto.enums.Order;
import com.beyt.exception.GenericFilterNoAvailableOrOperationUsageException;
import com.beyt.testenv.entity.Customer;
import com.beyt.testenv.entity.User;
import com.beyt.testenv.repository.CustomerRepository;
import com.beyt.testenv.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import javax.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.beyt.filter.query.simplifier.QuerySimplifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseFilterManagerTest {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public final User user1;
    public final User user2;
    public final User user3;
    public final User user4;
    public final User user5;
    public final User user6;
    public final User user7;
    public final User user8;

    public final Customer customer1;
    public final Customer customer2;
    public final Customer customer3;
    public final Customer customer4;
    public final Customer customer5;
    public final Customer customer6;
    public final Customer customer7;
    public final Customer customer8;
    public static final Calendar INSTANCE = Calendar.getInstance();

    DatabaseFilterManagerTest() {
        user1 = new User(null, "Name 1", "Surname 1", 35, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer1 = new Customer(null, "Customer 1", 20, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user1);
        INSTANCE.add(Calendar.MONTH, -1);
        user2 = new User(null, "Name 2", "Surname 1", 36, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer2 = new Customer(null, "Customer 2", 21, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user2);
        INSTANCE.add(Calendar.MONTH, -1);
        user3 = new User(null, "Name 3", "Surname 1", 37, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer3 = new Customer(null, "Customer 3", 22, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user3);
        INSTANCE.add(Calendar.MONTH, -1);
        user4 = new User(null, "Name 4", "Surname 1", 38, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer4 = new Customer(null, "Customer 4", 23, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user4);
        INSTANCE.add(Calendar.MONTH, -1);
        user5 = new User(null, "Name 5", "Surname 1", 39, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer5 = new Customer(null, "Customer 5", 24, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user5);
        INSTANCE.add(Calendar.MONTH, -1);
        user6 = new User(null, "Name 6", "Surname 1", 40, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer6 = new Customer(null, "Customer 6", 25, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user6);
        INSTANCE.add(Calendar.MONTH, -1);
        user7 = new User(null, "Name 7", "Surname 1", 41, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer7 = new Customer(null, "Customer 7", 26, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user7);
        INSTANCE.add(Calendar.MONTH, -1);
        user8 = new User(null, "Name 8", "Surname 1", 42, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()));
        customer8 = new Customer(null, null, 27, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault()), user8);

    }

    @BeforeAll
    private void init() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        customerRepository.save(customer5);
        customerRepository.save(customer6);
        customerRepository.save(customer7);
        customerRepository.save(customer8);
    }


    @Test
    void findAll() {
        // SINGLE CRITERIA TESTS

        //Support Single Input => GREATER_THAN_OR_EQUAL, GREATER_THAN, LESS_THAN_OR_EQUAL, LESS_THAN
        assertEquals(toList(customer5, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.GREATER_THAN_OR_EQUAL, 24))));
        assertEquals(toList(customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.GREATER_THAN, 24))));
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.LESS_THAN_OR_EQUAL, 24))));
        assertEquals(toList(customer1, customer2, customer3, customer4),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.LESS_THAN, 24))));

        INSTANCE.add(Calendar.MONTH, 3);
        assertEquals(toList(customer5, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.LESS_THAN_OR_EQUAL, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault())))));
        assertEquals(toList(customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.LESS_THAN, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault())))));
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.GREATER_THAN_OR_EQUAL, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault())))));
        assertEquals(toList(customer1, customer2, customer3, customer4),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.GREATER_THAN, ZonedDateTime.ofInstant(INSTANCE.toInstant(), ZoneId.systemDefault())))));

        // Support Multi Input => EQUAL, NOT_EQUAL
        assertEquals(toList(customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.EQUAL, 24))));
        assertEquals(toList(customer5, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.EQUAL, 24, 25, 26))));
        assertEquals(toList(customer4, customer5, customer6),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.EQUAL, 23, 24, 25))));
        assertEquals(toList(),
                customerRepository.findAllWithCriteria(                        CriteriaFilter.of(Criteria.of("age", CriteriaType.EQUAL,  24), Criteria.of("age", CriteriaType.EQUAL, 25))));
        assertEquals(toList(customer6),
                customerRepository.findAllWithCriteria(                        CriteriaFilter.of(Criteria.of("age", CriteriaType.EQUAL, 23, 24, 25), Criteria.of("age", CriteriaType.EQUAL, 25, 26))));

        assertEquals(toList(customer1, customer2, customer3, customer4, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.NOT_EQUAL, 24))));
        assertEquals(toList(customer1, customer2, customer3, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.NOT_EQUAL, 23, 24))));
        assertEquals(toList(customer1, customer2, customer3, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("age", CriteriaType.NOT_EQUAL, 23, 24, 25))));

        // Support Multi Input => CONTAIN, DOES_NOT_CONTAIN, START_WITH, END_WITH
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.CONTAIN, "Customer"))));
        assertEquals(toList(customer1, customer2),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.CONTAIN, "1", "2"))));
        assertEquals(toList(customer1, customer2, customer3, customer4, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.DOES_NOT_CONTAIN, "5"))));
        assertEquals(toList(customer1, customer2, customer3, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.DOES_NOT_CONTAIN, "5", "4"))));
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.START_WITH, "Customer"))));
        assertEquals(toList(customer3, customer4),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.START_WITH, "Customer 3", "Customer 4"))));
        assertEquals(toList(customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.END_WITH, " 7"))));
        assertEquals(toList(customer5, customer6),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.END_WITH, "5", "6"))));

        // Null, Not Null check. Support Single Input => SPECIFIED
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.SPECIFIED, true))));
        assertEquals(toList(customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.SPECIFIED, false))));

        // AND CRITERIA TESTs

        assertEquals(toList(), // Empty Result
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 24),
                        Criteria.of("age", CriteriaType.EQUAL,  25))));

        assertEquals(toList(customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 24),
                        Criteria.of("name", CriteriaType.EQUAL,  "Customer 5"))));

        assertEquals(toList(customer1, customer2, customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 20, 21 ,24, 25, 26), // OR 20, 21, 24, 25
                        Criteria.of("age", CriteriaType.NOT_EQUAL,  25, 26)))); // AND NOT 25 AND NOT 26


        assertEquals(toList(customer1, customer2),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 20, 21 ,24, 25, 26), // OR 20, 21, 24, 25
                        Criteria.of("age", CriteriaType.NOT_EQUAL,  25, 26),// AND NOT 25 AND NOT 26
                        Criteria.of("age", CriteriaType.LESS_THAN, 24)))); // AND LESS THAN 24


        assertEquals(toList(customer1),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 20, 21, 24, 25, 26), // OR 20, 21, 24, 25
                        Criteria.of("age", CriteriaType.NOT_EQUAL,  25, 26), // AND NOT 25 AND NOT 26
                        Criteria.of("age", CriteriaType.LESS_THAN, 24), // AND LESS THAN 24
                        Criteria.of("name", CriteriaType.CONTAIN, "1")))); // AND CONTAIN name 1


        // OR CRITERIA TESTs
        assertThrows(GenericFilterNoAvailableOrOperationUsageException.class, () ->
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("", CriteriaType.OR))));
        assertThrows(GenericFilterNoAvailableOrOperationUsageException.class, () ->
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("name", CriteriaType.EQUAL, "Customer 1"), Criteria.of("", CriteriaType.OR))));
        assertThrows(GenericFilterNoAvailableOrOperationUsageException.class, () ->
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("", CriteriaType.OR), Criteria.of("name", CriteriaType.EQUAL, "Customer 1"))));

        assertEquals(toList(customer1, customer2),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("name", CriteriaType.EQUAL, "Customer 1"), Criteria.of("", CriteriaType.OR),
                        Criteria.of("name", CriteriaType.EQUAL, "Customer 2"))));

        assertEquals(toList(customer1, customer2, customer3),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("name", CriteriaType.EQUAL, "Customer 1"),
                        Criteria.of("", CriteriaType.OR),
                        Criteria.of("name", CriteriaType.EQUAL, "Customer 2"),
                        Criteria.of("", CriteriaType.OR),
                        Criteria.of("name", CriteriaType.EQUAL, "Customer 3")
                )));

        assertEquals(toList(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 24),
                        Criteria.of("", CriteriaType.OR),
                        Criteria.of("age", CriteriaType.NOT_EQUAL,  24))));

        assertEquals(toList(customer4, customer5, customer6, customer7),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(
                        Criteria.of("age", CriteriaType.EQUAL, 23, 24),
                        Criteria.of("age", CriteriaType.NOT_EQUAL, 20, 21),
                        Criteria.of("", CriteriaType.OR), // ( [ (23 or 24) AND (not 20 and not 21) ] "OR" [ (not 24) AND (25 or 26) ])
                        Criteria.of("age", CriteriaType.NOT_EQUAL,  24),
                        Criteria.of("age", CriteriaType.EQUAL,  25, 26))));


    }

    public static <T> List<T> toList(T... values) {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, values);
        return list;
    }

    @Test
    void searchQuery() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.getWhere().add(Criteria.of("age", CriteriaType.GREATER_THAN, 20));
        searchQuery.setPageSize(10);
        searchQuery.setPageNumber(0);
        searchQuery.setDistinct(true);
        searchQuery.getOrderBy().add(Pair.of("age", Order.ASC));

        SearchQuery searchQuery1 = new SearchQuery();
        searchQuery1.getSelect().add(Pair.of("id", "id"));
        searchQuery1.getSelect().add(Pair.of("name", "name"));
        SearchQuery searchQuery2 = new SearchQuery();

        List<Customer> allWithSearchQuery1 = customerRepository.findAllWithSearchQuery(searchQuery1, Customer.class);
        List<User> allWithSearchQuery2 = customerRepository.findAllWithSearchQuery(searchQuery1, User.class);
        List<User> allWithSearchQuery3 = customerRepository.findAllWithSearchQuery(searchQuery2, User.class);
        List<Customer> allWithSearchQuery4 = customerRepository.findAllWithSearchQuery(searchQuery2, Customer.class);
    }

    @Test
    void simplifiedSearchQuery() {
        List<User> result2 = customerRepository.query()
                .select(Select("user.name", "name"), Select("user.age"), Select("name", "surname"), Select("birthdate", "birthdate"))
                .distinct(false)
                .where(Parantesis(Field("id").eq(3), OR, Field("user.id").eq(4), OR, Field("id").eq(5)), Parantesis(Field("id").eq(6), OR, Field("id").eq(4), OR, Field("user.id").eq(5)))
                .orderBy(OrderBy("user.name", Order.ASC))
                .page(0, 5)
                .getResult(User.class);

    }
}
