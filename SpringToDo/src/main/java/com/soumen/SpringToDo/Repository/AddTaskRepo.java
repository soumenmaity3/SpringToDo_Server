package com.soumen.SpringToDo.Repository;

import com.soumen.SpringToDo.Model.ToDo;
import com.soumen.SpringToDo.Model.User;
import com.soumen.SpringToDo.Summary.ToDoSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddTaskRepo extends JpaRepository<ToDo,Integer> {
    @Query(value = "SELECT description,tittle FROM to_do WHERE completed=true AND email=:email",nativeQuery = true)
    List<ToDoSummary> findByCompletedTrueAndEmail(String email);
    @Query(value = "SELECT description,tittle,id,completed,email FROM to_do WHERE completed=false AND email=:email",nativeQuery = true)
    List<ToDo> findByCompletedFalseAndEmail(String email);
}
