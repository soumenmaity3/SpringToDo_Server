package com.soumen.SpringToDo.Repository;

import com.soumen.SpringToDo.Model.ToDo;
import com.soumen.SpringToDo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddTaskRepo extends JpaRepository<ToDo,Integer> {
}
