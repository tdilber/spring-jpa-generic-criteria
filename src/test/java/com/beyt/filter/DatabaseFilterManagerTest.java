package com.beyt.filter;

import com.beyt.BaseTestInstance;
import com.beyt.TestApplication;
import com.beyt.dto.Criteria;
import com.beyt.dto.CriteriaFilter;
import com.beyt.dto.SearchQuery;
import com.beyt.dto.enums.CriteriaType;
import com.beyt.dto.enums.Order;
import com.beyt.exception.GenericFilterNoAvailableOrOperationUsageException;
import com.beyt.testenv.entity.Customer;
import com.beyt.testenv.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.beyt.filter.query.simplifier.QuerySimplifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseFilterManagerTest extends BaseTestInstance {

    private SimpleDateFormat dateFormat;

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
        dateFormat = new SimpleDateFormat();
        assertEquals(toList(customer5, customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.LESS_THAN_OR_EQUAL, INSTANCE.toInstant()))));
        assertEquals(toList(customer6, customer7, customer8),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.LESS_THAN, INSTANCE.toInstant()))));
        assertEquals(toList(customer1, customer2, customer3, customer4, customer5),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.GREATER_THAN_OR_EQUAL, INSTANCE.toInstant()))));
        assertEquals(toList(customer1, customer2, customer3, customer4),
                customerRepository.findAllWithCriteria(CriteriaFilter.of(Criteria.of("birthdate", CriteriaType.GREATER_THAN, INSTANCE.toInstant()))));

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
        List<User> result = customerRepository.query()
                .select(Select("user.name", "name"), Select("user.age"), Select("name", "surname"), Select("birthdate", "birthdate"))
                .distinct(false)
                .where(Parantesis(Field("id").eq(3), OR, Field("user.id").eq(4), OR, Field("id").eq(5)), OR, Parantesis(Field("id").eq(6), OR, Field("id").eq(4), OR, Field("user.id").eq(5)))
                .orderBy(OrderBy("user.id", Order.ASC))
                .page(0, 2)
                .getResult(User.class);

        assertEquals(toList(new User(null, user3.getName(), customer3.getName(), user3.getAge(), customer3.getBirthdate()),
                new User(null, user4.getName(), customer4.getName(), user4.getAge(), customer4.getBirthdate())), result);

        Page<User> resultAsPage = customerRepository.query()
                .select(Select("user.name", "name"), Select("user.age"), Select("name", "surname"), Select("birthdate", "birthdate"))
                .distinct(false)
                .where(Parantesis(Field("id").eq(3), OR, Field("user.id").eq(4), OR, Field("id").eq(5)), OR, Parantesis(Field("id").eq(6), OR, Field("id").eq(4), OR, Field("user.id").eq(5)))
                .orderBy(OrderBy("user.id", Order.ASC))
                .page(1, 2)
                .getResultAsPage(User.class);

        assertEquals(toList(new User(null, user5.getName(), customer5.getName(), user5.getAge(), customer5.getBirthdate()),
                new User(null, user6.getName(), customer6.getName(), user6.getAge(), customer6.getBirthdate())), resultAsPage.getContent());

        assertEquals(4, resultAsPage.getTotalElements());
        assertEquals(2, resultAsPage.getTotalPages());
        assertEquals(Sort.Direction.ASC, resultAsPage.getPageable().getSort().getOrderFor("user.id").getDirection());
    }
}
