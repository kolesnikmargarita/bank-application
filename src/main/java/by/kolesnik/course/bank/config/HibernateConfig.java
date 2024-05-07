package by.kolesnik.course.bank.config;

import by.kolesnik.course.bank.entity.Account;
import by.kolesnik.course.bank.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();

        return new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .buildMetadata()
                .buildSessionFactory();
    }


}
