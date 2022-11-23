package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserService us = new UserServiceImpl();
        /*
        us.createUsersTable();

        User u1 = new User("Вася1", "Пупкин1", (byte) 2);
        User u2 = new User("Вася2", "Пупкин2", (byte) 24);
        User u3 = new User("Вася3", "Пупкин3", (byte) 50);
        User u4 = new User("Вася4", "Пупкин4", (byte) 84);
        User[] users = {u1, u2, u3, u4};
        for (User u: users) {
            us.saveUser(u.getName(), u.getLastName(), u.getAge());
            System.out.println("User с именем – " + u.getName() + " добавлен в базу данных");
        }

        List<User> allUsers = us.getAllUsers();
        allUsers.forEach(System.out::println);

        us.cleanUsersTable();

         */
        us.dropUsersTable();
    }
}
