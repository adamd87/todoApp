package pl.adamd.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.adamd.model.TaskGroup;
import pl.adamd.model.TaskGroupRepository;
import pl.adamd.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when at least one task is undone")
    void toggleGroup_undoneTask_throwsIllegalStateException() {
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        // System under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Done all the tasks first");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when TaskGroup was not found")
    void toggleGroup_TaskGroupDoesNotExists_throwsIllegalArgumentException() {
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        // System under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TaskGroup with given id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_TaskGroupDoesExists_AndAllTasksAreDone_toggleGroupAsDone() {
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        // and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        // System under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        // when
        toTest.toggleGroup(0);
        // then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }



}