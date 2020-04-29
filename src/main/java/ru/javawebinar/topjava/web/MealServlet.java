package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
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

public class MealServlet extends HttpServlet {
    private static final LocalTime START_TIME = LocalTime.of(0, 0);
    private static final LocalTime END_TIME = LocalTime.of(23, 59);
    private static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private MealStorage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MapMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        final List<MealTo> mealsTo = getMealsTo();
        req.setAttribute("mealsTo", mealsTo);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private List<MealTo> getMealsTo() {
        final List<Meal> meals = storage.getAll();
        return MealsUtil.getFiltered(meals, START_TIME, END_TIME, DEFAULT_CALORIES_PER_DAY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        final String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect("meals");
            return;
        }
        final Integer id = getId(req);
        Meal meal;
        switch (action) {
            case "create":
                saveMeal(req, id);
                resp.sendRedirect("meals");
                return;
            case "add":
            case "edit":
                meal = storage.get(id);
                break;
            case "delete":
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            default:
                throw new IllegalArgumentException("Action " + action + "is not valid");
        }
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("/newMeal.jsp").forward(req, resp);
    }

    private void saveMeal(HttpServletRequest req, Integer id) {
        final LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("date") + 'T' + req.getParameter("time"));
        final String description = req.getParameter("description");
        final int calories = Integer.parseInt(req.getParameter("calories"));
        final Meal meal = new Meal(id, dateTime, description, calories);
        storage.save(meal);
    }

    private Integer getId(HttpServletRequest req) {
        final String reqId = req.getParameter("id");
        return reqId == null || reqId.isEmpty() ? null : Integer.parseInt(reqId);
    }
}