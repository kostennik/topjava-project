package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.meal.JspMealController.PATH;

@Controller
@RequestMapping(PATH)
public class JspMealController extends AbstractMealController {
    public static final String PATH = "/meals";

    @GetMapping("/create")
    public String getForNew(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        log.info("get new meal for user {}", userId);
        return "mealForm";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            super.create(getMeal(request));
        }
        return "redirect:" + PATH;
    }

    @GetMapping("/update")
    public String getForUpdate(HttpServletRequest request) {
        int id = getId(request);
        request.setAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping("/update")
    public String update(HttpServletRequest request) {
        int id = getId(request);
        Meal meal = getMeal(request);
        super.update(meal, id);
        return "redirect:" + PATH;
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        super.delete(id);
        return "redirect:" + PATH;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getBetween(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal getMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }
}
