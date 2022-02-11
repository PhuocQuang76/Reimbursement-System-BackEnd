/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.revature;

import com.revature.controllers.AuthController;
import com.revature.controllers.UserController;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoJDBC;
import com.revature.routes.AuthRoutes;
import com.revature.routes.Route;
import com.revature.routes.UserRoutes;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.Map;

public class JavalinDriver {
    private static UserDao userDao = new UserDaoJDBC();
    private static UserService userService= new UserService(userDao);

    private static AuthService authService = new AuthService(userDao);
    private static AuthController authController = new AuthController(authService, userService);

    public static void main(String[] args) {
//        //Establish our Javalin app
        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.addStaticFiles("/static", Location.CLASSPATH);
        });

        UserRoutes userRoutes = new UserRoutes(new UserController(userService));
//
//        //Creating our first handler, but for the rest we are going to break the routes into controllers to handle functionality for each of our services
        app.get("/hello", (ctx) -> ctx.result("Hello we are making our first get!"));
//
        Route auth = new AuthRoutes(authController);
        Route.establishRoutes(app,auth, userRoutes);

        app.error(403, (ctx) -> {
            ctx.result("The request you submitted is invalid");
        });

//        app.exception(AssignmentPastDueException.class, (e, ctx) -> {
//            ctx.status(400);
//            ctx.result("You turned in your assignment in late");
//        });

        app.start(7000);
    }
}