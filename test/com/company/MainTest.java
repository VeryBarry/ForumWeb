package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Created by VeryBarry on 10/13/16.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "pass");
        User user = Main.selectUser(conn, "Alice");
        conn.close();
        assertTrue(user != null);
    }
    @Test
    public void testMessage() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "pass");
        User user = Main.selectUser(conn, "Alice");
        Main.insertMessage(conn, -1, "Hello, world.", user.id);
        Message message = Main.selectMessage(conn, 1);
        conn.close();
        assertTrue(message != null);
        assertTrue(message.author.equals("Alice"));
    }
    @Test
    public void testReplies() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "pass");
        Main.insertUser(conn, "Bob", "pass");
        User alice = Main.selectUser(conn, "Alice");
        User bob = Main.selectUser(conn, "Bob");
        Main.insertMessage(conn, -1, "Hello, world.", alice.id);
        Main.insertMessage(conn, 1, "Hey Alice.", bob.id);
        Main.insertMessage(conn, 1, "Hey again, Alice.", bob.id);
        ArrayList<Message> replies = Main.selectReplies(conn, 1);
        conn.close();
        assertTrue(replies.size() == 2);
    }
}