package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.repository.BaseRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractInMemoryRepository<T extends AbstractBaseEntity> implements BaseRepository<T> {
    protected static final Logger log = LoggerFactory.getLogger(AbstractInMemoryRepository.class);

    protected Map<Integer, T> repository = new ConcurrentHashMap<>();
    protected AtomicInteger counter = new AtomicInteger(0);

    @Override
    public T save(T entity) {
        log.info("save {}", entity);
        if (entity.isNew()) {
            entity.setId(counter.incrementAndGet());
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfPresent(entity.getId(), (id, oldMeal) -> entity);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public T get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }
}