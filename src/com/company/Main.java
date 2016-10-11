package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();
    static ArrayList<Message> messages = new ArrayList<>();

    public static void main(String[] args) {
        users.put("Alice", new User("Alice", "pass"));
        users.put("Bob", new User("Bob", "pass"));
        users.put("Charlie", new User("Charlie", "pass"));

        messages.add(new Message(0, -1, "Alice", "Hello Everybody"));
        messages.add(new Message(1, -1, "Bob", "How's it going er'body"));
        messages.add(new Message(2, 0, "Charlie", "Cool Thread, Alice"));
        messages.add(new Message(3, 2, "Alice", "Thanks Charlie"));

        Spark.get(
                "/",
                (request, response) -> {
                    String replyId = request.queryParams("replyId");
                    int replyIdNum = -1;
                    if (replyId != null){
                        replyIdNum = Integer.valueOf(replyId);
                    }

                    Session session = request.session();
                    String name = session.attribute("userName");

                    HashMap m = new HashMap();
                    ArrayList<Message> msgs = new ArrayList<>();
                    for (Message message : messages) {
                        if (message.replyId == replyIdNum) {
                            msgs.add(message);
                        }
                    }
                    m.put("messages", msgs);
                    m.put("name", name);
                    return new ModelAndView(m, "home.html");
                },
        new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("userName");
                    String pass = request.queryParams("passWord");
                    User user = users.get(name);
                    if(user == null){
                        user = new User(name, pass);
                        users.put(name, user);
                    }
                    else if (!pass.equals(user.passWord)){
                        Spark.halt(403);
                        return null;
                    }
                    Session session = request.session();
                    session.attribute("userName", name);
                    response.redirect("/");
                    return null;
                },
                new MustacheTemplateEngine()
        );

    }
}