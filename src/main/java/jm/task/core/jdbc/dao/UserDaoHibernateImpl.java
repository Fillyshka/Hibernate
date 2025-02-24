package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory;

    static{
        try{
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);

            configuration.getProperties().put("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.getProperties().put("hibernate.connection.url", "jdbc:postgresql://localhost:5433/hibernate");
            configuration.getProperties().put("hibernate.connection.username", "postgres");
            configuration.getProperties().put("hibernate.connection.password", "2001");
            configuration.getProperties().put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.getProperties().put("hibernate.show_sql", "true");
            configuration.getProperties().put("hibernate.current_session_context_class", "thread");
            configuration.getProperties().put("hibernate.hbm2ddl.auto", "none");

            sessionFactory=configuration.buildSessionFactory();
        }catch(Exception e){
            throw new ExceptionInInitializerError(e);
        }
    }
    private static SessionFactory getSessionFactory(){
        return sessionFactory;
    }


    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            session.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS users(" +
                            "id SERIAL PRIMARY KEY," +
                            "name VARCHAR(255)," +
                            "lastname VARCHAR(255)," +
                            "age SMALLINT)").executeUpdate();
            t.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            t.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            t.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()){
            Transaction t = session.beginTransaction();
            User user = session.get(User.class,id);
            if ( user !=null){
                session.delete(user);
            }
            t.commit();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            t.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
