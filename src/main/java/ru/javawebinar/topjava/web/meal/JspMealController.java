package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/mealForm")
    public String get(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("update for user {}", userId);
        model.addAttribute("meal", mealService.get(id, userId));
        return "mealForm";
    }

    @PostMapping("/get")
    public String update(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("get meal {} for user {}", id, userId);
        model.addAttribute("id", id);
        model.addAttribute("meal", mealService.get(id, userId));
        return "redirect:mealForm";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        return "redirect:meals";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        Meal meal = new Meal(
                id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info("update {} for user {}", meal, userId);
        mealService.update(meal, userId);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
