package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.*;
import org.hibernate.query.NativeQuery;

import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    final static SessionFactory sf = getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        // не те типы полей получаются: вместо bit(8) получается tinyint,
        // вместо varchar(32) получается по умолчанию varchar(255)
        // как задать тип поля в создаваемой таблице точно такой, какой хочу - не нашла
        getAllUsers();
    }

    @Override
    public void dropUsersTable() {
        Transaction transactionObj;
        try (Session sessionObj = sf.openSession()) {
            transactionObj = sessionObj.beginTransaction();
            NativeQuery query = sessionObj.createSQLQuery("DROP TABLE IF EXISTS user");
            //Я вижу, что это интерфейс и idea предупреждает про raw use,
            //но я не нашла имплементацию, которую бы сюда прикрутить
            //TypedQuery<T> query, но мне не нужен тут тип...
            //NativeQueryImpl, но он деприкейтид
            try {
                query.executeUpdate();
                transactionObj.commit();
            } catch (Exception e) { //я тут не переписываю все возможные в этих командах исключения,
                                    // так нормально?
                if (transactionObj != null) {
                    transactionObj.rollback();
                }
            }
        } catch (HibernateException e) {    //странная конструкция, зачем-то подменяю исключения,
                                            // списала где-то, но не уверена, зачем
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transactionObj;
        try (Session sessionObj = sf.openSession()) {
            transactionObj = sessionObj.beginTransaction();
            User newUser = new User(name, lastName, age);
            try {
                sessionObj.save(newUser);
                transactionObj.commit();
                System.out.println("\nSuccessfully Created '" + name);
            } catch (Exception e) {
                if (transactionObj != null) {
                    transactionObj.rollback();
                }
            }
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transactionObj;
        try (Session sessionObj = sf.openSession()) {
            transactionObj = sessionObj.beginTransaction();
            User user = sessionObj.get(User.class, id);
            try {
                sessionObj.remove(user);
                transactionObj.commit();
            } catch (Exception e) {
                if (transactionObj != null) {
                    transactionObj.rollback();
                }
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transactionObj;
        List<User> userList = null;
        try (Session sessionObj = sf.openSession()) {
            transactionObj = sessionObj.beginTransaction();
            try {
                userList = sessionObj.createQuery("SELECT u FROM User u", User.class)
                        .getResultList();
                transactionObj.commit();
            } catch (Exception e) {
                if (transactionObj != null) {
                    transactionObj.rollback();
                }
            }

        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transactionObj;
        try (Session sessionObj = sf.openSession()) {
            transactionObj = sessionObj.beginTransaction();
            NativeQuery query = sessionObj.createSQLQuery("DELETE FROM user");
            try {
                query.executeUpdate();
                transactionObj.commit();
            } catch (Exception e) {
                if (transactionObj != null) {
                    transactionObj.rollback();
                }
            }
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
}
