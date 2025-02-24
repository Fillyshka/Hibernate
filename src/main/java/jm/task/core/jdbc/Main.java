package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoHibernateImpl();
        UserService userService = new UserServiceImpl(userDao);

        userService.createUsersTable();
        System.out.println("Таблица пользователей создана.");

        userService.saveUser("dima", "Shah", (byte) 18);
        userService.saveUser("roma", "SmAh", (byte) 16);
        userService.saveUser("kola", "BroH", (byte) 10);
        userService.saveUser("olga", "Dav", (byte) 19);

        List<User> users = userService.getAllUsers();
        System.out.println("Все пользователи в базе данных:");
        for (User user : users) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        System.out.println("Таблица пользователей очищена.");

        userService.dropUsersTable();
        System.out.println("Таблица пользователей удалена.");

    }
}
