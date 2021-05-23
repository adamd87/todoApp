package pl.adamd.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.adamd.model.Task;
import pl.adamd.model.TaskRepository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
}
