package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.web.meal.JspMealController.PATH;

@Controller
@RequestMapping(PATH)
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);
    public static final String PATH = "/meals";
    public static final String MEAL_FORM_PATH = "/mealForm";
    public static final String UPDATE_PATH = "/update";
    public static final String DELETE_PATH = "/delete";

    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(UPDATE_PATH)
    public String get(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("update for user {}", userId);
        model.addAttribute("meal", mealService.get(id, userId));
        return "mealForm";
    }

    @GetMapping(DELETE_PATH)
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        final StringBuffer requestURL = request.getRequestURL();
        requestURL.setLength(requestURL.length() - DELETE_PATH.length());
        return "redirect:" + requestURL.toString();
    }

    @PostMapping(MEAL_FORM_PATH)
    public String update(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        Meal meal = new Meal(
                id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info("update {} for user {}", meal, userId);
        mealService.update(meal, userId);
        final StringBuffer requestURL = request.getRequestURL();
        requestURL.setLength(requestURL.length() - MEAL_FORM_PATH.length());
        return "redirect:" + requestURL.toString();
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
