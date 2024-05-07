package by.kolesnik.course.bank.repository;

import by.kolesnik.course.bank.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // найти по id
    public Optional<User> findById(Long id) {
        return sessionFactory.fromTransaction(session -> {
            final Query<User> query = session.createQuery("from User U where U.id = :id", User.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        });
    }

    // найти всех
    public List<User> findAll() {
        return sessionFactory.fromTransaction(session -> {
            final Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        });
    }

    // обновить всю информацию о пользователе
    /*public User update(Long id, User user) {
        return sessionFactory.fromTransaction(session -> {
            final MutationQuery query = session.createMutationQuery("""
update User U set U.firstName = :firstName,
U.lastName = :lastName,
U.phoneNumber = :phoneNumber
where U.id = :id
""");
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("phoneNumber", user.getPhoneNumber());
            query.setParameter("id", id);

            query.executeUpdate();

            return user;
        });
    }*/
    // обновить часть информации о пользователе
    public User save(User user) {
        return sessionFactory.fromTransaction(session -> {
            if(user.getId() == null) {
                session.persist(user);
            } else {
                session.merge(user);
            }
            return user;
        });
    }

    // удалить пользователя
    public void removeById(Long id) {
        sessionFactory.inTransaction(session -> {
            final MutationQuery query = session.createMutationQuery("delete from User U where U.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        });
    }
}
