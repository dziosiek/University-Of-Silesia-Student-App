package com.api.us.restapi.student_group;

import com.api.us.restapi.my.exceptions.NotFoundException;
import com.api.us.restapi.user.User;
import com.api.us.restapi.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentGroupRestController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;

    @GetMapping("jpa/student-groups")
    public List<StudentGroup> getStudentGroup(){
        return studentGroupRepository.findAll();
    }

    @PostMapping("jpa/student-groups")
    public ResponseEntity<Object> addNewGroup(@Valid @RequestBody StudentGroup group) throws URISyntaxException {
        StudentGroup groupSaved = studentGroupRepository.save(group);
        Resource<StudentGroup> resource = new Resource<>(groupSaved);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(groupSaved.getId()).toUri();
        return ResponseEntity.created(location).body(groupSaved);
    }

    @DeleteMapping("jpa/student-groups/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Integer id){
        studentGroupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("jpa/student-groups/{id}/users")
    public List<User> getUsersFromGroup(@PathVariable Integer id){
       Optional<StudentGroup> byId = studentGroupRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException("id group:"+Integer.toString(id) +" not found");
        }
        return byId.get().getUserList();
    }
}
