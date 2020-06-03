package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.getEndExclusive;
import static ru.javawebinar.topjava.util.DateTimeUtil.getStartInclusive;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() || get(meal.getId(), userId) != null) {
            meal.setUser(userRepository.getOne(userId));
            return mealRepository.save(meal);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return mealRepository.deleteMealByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return mealRepository.findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(
                userId, getStartInclusive(startDate), getEndExclusive(endDate));
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealRepository.getWithUser(id, userId);
    }
}
