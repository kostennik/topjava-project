package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;

import javax.validation.Valid;
import java.util.List;

import static ru.javawebinar.topjava.web.BindingResultUtil.getBindingResultResponseEntity;

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createOrUpdate(@Valid UserTo userTo, BindingResult result) {
        ResponseEntity<String> responseEntity = getBindingResultResponseEntity(result);
        if (responseEntity != null) {
            return responseEntity;
        }
        if (userTo.isNew()) {
            User user = UserUtil.createNewFromTo(userTo);
            super.create(user);
        } else {
            User user = super.get(userTo.id());
            User updatedUser = UserUtil.updateFromTo(user, userTo);
            super.update(updatedUser, updatedUser.id());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}
