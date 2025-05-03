package com.soumen.SpringToDo.Service;

import com.soumen.SpringToDo.Model.ToDo;
import com.soumen.SpringToDo.Model.User;
import com.soumen.SpringToDo.Repository.AddTaskRepo;
import com.soumen.SpringToDo.Repository.ToDoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private ToDoRepo repo;

    @Transactional
    public boolean deleteByEmailAndPassword(String email, String password) {
        Optional<User> present=repo.findByEmailAndPassword(email,password);
        if (present.isPresent()){
            repo.deleteByEmailAndPassword(email,password);
            return true;
        }else {
            return false;
        }
    }


    public List<ToDo> getTodosByEmail(String email) {
        Optional<User> user = repo.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getTodos();
        }
        return new ArrayList<>();
    }

}