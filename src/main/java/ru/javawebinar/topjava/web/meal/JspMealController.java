package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.web.meal.JspMealController.PATH;

@Controller
@RequestMapping(PATH)
public class JspMealController extends AbstractMealController{
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);
    public static final String PATH = "/meals";

    @GetMapping("/create")
    public String get(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        log.info("get new meal for user {}", userId);
        return "mealForm";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            log.info("create {} for user {}", meal, userId);
            mealService.create(meal, userId);
        }
        return "redirect:" + PATH;
    }

    @GetMapping("/update")
    public String getForUpdate(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("get meal {} for user {}", id, userId);
        request.setAttribute("meal", mealService.get(id, userId));
        return "mealForm";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int id = getId(request);
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        mealService.update(meal, userId);
        return "redirect:" + PATH;
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        return "redirect:" + PATH;
    }

    @GetMapping
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/filter")
    public String getBetween(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();

        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, userId);
        request.setAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
