package com.soumen.SpringToDo.Controller;

import com.soumen.SpringToDo.Model.EmailRequest;
import com.soumen.SpringToDo.Model.LoginModel;
import com.soumen.SpringToDo.Model.ToDo;
import com.soumen.SpringToDo.Model.User;
import com.soumen.SpringToDo.OtpUtils;
import com.soumen.SpringToDo.Repository.AddTaskRepo;
import com.soumen.SpringToDo.Repository.ToDoRepo;
import com.soumen.SpringToDo.Service.EmailService;
import com.soumen.SpringToDo.Service.UserService;
import com.soumen.SpringToDo.Summary.ToDoSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping({"/","users"})
public class UsersController {
    @Autowired
    private UserService service;
    @Autowired
    private ToDoRepo repo;
    @Autowired
    private AddTaskRepo repo2;


    @GetMapping("ping")
    public ResponseEntity<?> responseEntity(){
        return new ResponseEntity<>("Server is healthy",HttpStatus.OK);
    }


    @PostMapping("signup")
    public ResponseEntity<?> addUser(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("user_name")String user_name){
        if (email.isEmpty() || password.isEmpty() || user_name.isEmpty()) {
            return new ResponseEntity<>("Enter All the Details",HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<User>checkUser=repo.findByEmail(email);
        if (checkUser.isPresent()) {
            return new ResponseEntity<>("Already have an account of this email",HttpStatus.CONFLICT);
        }
        try{
            int result=repo.register(email,password,user_name);
            if (result==1){
                Map<String,String> map=new HashMap<>();
                map.put("message","User SignUp Successful");
                return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
            }else {
                Map<String ,String>map=new HashMap<>();
                map.put("message","Something want wrong");
                return new ResponseEntity<>(map,HttpStatus.NOT_ACCEPTABLE);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Email already registered!", HttpStatus.CONFLICT);
        }
    }


@PostMapping("login")
public ResponseEntity<?> loginUser(@RequestBody LoginModel login) {
        List<String> emails = repo.checkUserEmail(login.getEmail());

        if (emails == null || emails.isEmpty()) {
            return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);
        }

        String storedPassword = repo.checkUserPasswordByEmail(login.getEmail());;
        if (storedPassword == null || !storedPassword.equals(login.getPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        List<ToDo> todos = service.getTodosByEmail(login.getEmail());
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping("/add_task")
    public ResponseEntity<?> addTask(@RequestBody ToDo toDo) {
        if (toDo.getUser() == null || toDo.getUser().getEmail() == null) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = repo.findByEmail(toDo.getUser().getEmail());

        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        toDo.setUser(user.get());
        repo2.save(toDo);
        return new ResponseEntity<>("Done", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete_task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id, @RequestParam String email) {
        Optional<ToDo> task = repo2.findById(id);

        if (task.isEmpty() || !task.get().getUser().getEmail().equals(email)) {
            return new ResponseEntity<>("Unauthorized or not found", HttpStatus.NOT_FOUND);
        }

        repo2.deleteById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/updateComplete/{id}")
    public ResponseEntity<String> updateComplete(@PathVariable int id) {
        Optional<ToDo> optionalTask = repo2.findById(id);
        if (optionalTask.isPresent()) {
            ToDo task = optionalTask.get();
            task.setCompleted(true);
            repo2.save(task);
            return new ResponseEntity<>("Task marked as complete", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateTask/{id}")
    public ResponseEntity<?> updateTask(@RequestBody ToDo toDo,@PathVariable int id){
        Optional<ToDo> optionalToDo=repo2.findById(id);
        if (optionalToDo.isPresent()) {
            ToDo task=optionalToDo.get();
            task.setTittle(toDo.getTittle());
            task.setDescription(toDo.getDescription());
            repo2.save(task);
            return new ResponseEntity<>("Update Done",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Failed",HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("delete-history")
    public ResponseEntity<?> deleteHistory(@RequestParam("email") String email){
        int delete=repo.deleteHistory(email);
        if (delete>0){
            return new ResponseEntity<>("Done",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Check",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete-user")
    public ResponseEntity<?>delete(@RequestParam("email")String email,
                                    @RequestParam("password")String password){
        boolean deleted=service.deleteByEmailAndPassword(email,password);

        if (deleted){
            return new ResponseEntity<>("Ok Accept",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PutMapping("/recover-data")
    public ResponseEntity<?> recoverData(@RequestParam("email") String email){
        int recover=repo.recoverDataByEmail(email);
        if (recover > 0) {
            return new ResponseEntity<>("Done",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Check Again",HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/user-name")
    public ResponseEntity<?> userName(@RequestParam("email") String email){
        if (email == null ||email.isEmpty()) {
            return new ResponseEntity<>("Enter the email",HttpStatus.BAD_REQUEST);
        }
        String username=repo.findUsernameByEmail(email);
        return new ResponseEntity<>(username,HttpStatus.OK);
    }

    @GetMapping("check-email")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email){
        if (email == null ||email.isEmpty()) {
            return new ResponseEntity<>("Enter the email",HttpStatus.BAD_REQUEST);
        }
        String email2=repo.checkUserEmail2(email);
        if (email2 == null) {
            return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);
        }else {
        return new ResponseEntity<>(email2,HttpStatus.OK);}
    }

    @PutMapping("reset-password")
    public ResponseEntity<?> restPassword(@RequestParam("email") String email,
                                          @RequestParam("password") String password){
        if (email == null ||email.isEmpty()&&password==null||password.isEmpty()) {
            return new ResponseEntity<>("Enter All Things",HttpStatus.BAD_REQUEST);
        }
        int res=repo.resatPassword(email,password);
        if (res > 0) {
            return new ResponseEntity<>("Done",HttpStatus.OK);
        }
        return new ResponseEntity<>("Error",HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("completed_task")
    public List<ToDoSummary> getCompletedTask(@RequestParam("email") String email){
        return repo2.findByCompletedTrueAndEmail(email);
    }
    @GetMapping("get_task")
    public List<ToDo>get_task(@Param("email") String email){
        return repo2.findByCompletedFalseAndEmail(email);
    }


    @Autowired
    private EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody EmailRequest request) {
        try {
            emailService.sendOtp(request.getEmail());
            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP");
        }
    }
    @GetMapping("/otp-checker")
    public ResponseEntity<?> otpChecker(@Param("otp") String otp){
        OtpUtils otpUtils=new OtpUtils();
        String otp2=otpUtils.otp();
//        String email2=repo.checkUserEmail2(email);
//        if (email2 == null) {
//            return new ResponseEntity<>("Enter Correct Email",HttpStatus.NOT_FOUND);
//        }
        if (otp2.equals(otp)){
            return new ResponseEntity<>("Correct",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("This is not work",HttpStatus.BAD_REQUEST);
        }
    }
}