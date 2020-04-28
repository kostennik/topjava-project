package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MapStorage storage = new MapStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MapStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("get User meals running");
        req.setCharacterEncoding("UTF-8");
        final String reqId = req.getParameter("id");
        final Integer id = reqId != null ? Integer.parseInt(reqId) : null;

        String action = req.getParameter("action");
        if (action == null) {
            final LocalTime startTime = LocalTime.of(7, 0);
            final LocalTime endTime = LocalTime.of(22, 0);
            final int DEFAULT_CALORIES_PER_DAY = 2000;

            List<Meal> meals = storage.getAll();
            List<MealTo> mealsTo = MealsUtil.getFiltered(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY);

            req.setAttribute("mealsTo", mealsTo);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        Meal meal;
        switch (action) {
            case "save" :
                meal = new Meal();
                break;
            case "delete" :
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            case "edit" :
                meal = storage.get(id);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + "is not valid");
        }
        req.setAttribute("meal" , meal);
        req.getRequestDispatcher("/newMeal.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("post User meals running");
        req.setCharacterEncoding("UTF-8");
        final String reqId = req.getParameter("id");
        final Integer id = !reqId.isEmpty() ? Integer.parseInt(reqId) : null;
        final String description = req.getParameter("description");
        final int calories = Integer.parseInt(req.getParameter("calories"));

        final LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("date"));

        Meal meal = new Meal(id, dateTime, description, calories);
        storage.save(meal);
        resp.sendRedirect("meals");
    }
}