package com.beyt;

import com.beyt.testenv.entity.Customer;
import com.beyt.testenv.entity.User;
import com.beyt.testenv.repository.CustomerRepository;
import com.beyt.testenv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Calendar;

public abstract class BaseTestInstance {

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected EntityManager entityManager;

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

    public BaseTestInstance() {
        user1 = new User(null, "Name 1", "Surname 1", 35, INSTANCE.toInstant(), User.Status.PASSIVE, User.Type.USER);
        customer1 = new Customer(null, "Customer 1", 20, INSTANCE.toInstant(), user1);
        INSTANCE.add(Calendar.MONTH, -1);
        user2 = new User(null, "Name 2", "Surname 1", 36, INSTANCE.toInstant(), User.Status.ACTIVE, User.Type.ADMIN);
        customer2 = new Customer(null, "Customer 2", 21, INSTANCE.toInstant(), user2);
        INSTANCE.add(Calendar.MONTH, -1);
        user3 = new User(null, "Name 3", "Surname 1", 37, INSTANCE.toInstant(), User.Status.PASSIVE, User.Type.USER);
        customer3 = new Customer(null, "Customer 3", 22, INSTANCE.toInstant(), user3);
        INSTANCE.add(Calendar.MONTH, -1);
        user4 = new User(null, "Name 4", "Surname 1", 38, INSTANCE.toInstant(), User.Status.ACTIVE, User.Type.USER);
        customer4 = new Customer(null, "Customer 4", 23, INSTANCE.toInstant(), user4);
        INSTANCE.add(Calendar.MONTH, -1);
        user5 = new User(null, "Name 5", "Surname 1", 39, INSTANCE.toInstant(), User.Status.PASSIVE, User.Type.ADMIN);
        customer5 = new Customer(null, "Customer 5", 24, INSTANCE.toInstant(), user5);
        INSTANCE.add(Calendar.MONTH, -1);
        user6 = new User(null, "Name 6", "Surname 1", 40, INSTANCE.toInstant(), User.Status.ACTIVE, User.Type.ADMIN);
        customer6 = new Customer(null, "Customer 6", 25, INSTANCE.toInstant(), user6);
        INSTANCE.add(Calendar.MONTH, -1);
        user7 = new User(null, "Name 7", "Surname 1", 41, INSTANCE.toInstant(), User.Status.ACTIVE, User.Type.USER);
        customer7 = new Customer(null, "Customer 7", 26, INSTANCE.toInstant(), user7);
        INSTANCE.add(Calendar.MONTH, -1);
        user8 = new User(null, "Name 8", "Surname 1", 42, INSTANCE.toInstant(), User.Status.PASSIVE, User.Type.ADMIN);
        customer8 = new Customer(null, null, 27, INSTANCE.toInstant(), user8);

    }
}
