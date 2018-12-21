package com.api.us.restapi.user;

import com.api.us.restapi.auth.Token;
import com.api.us.restapi.my.exceptions.ConflictException;
import com.api.us.restapi.my.exceptions.NotFoundException;

import com.api.us.restapi.student_group.StudentGroup;
import com.api.us.restapi.student_group.StudentGroupRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
public class UsersRestController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;

//    @CrossOrigin(origins = "http://192.168.1.106:8080")
    @GetMapping("jpa/users")
    public List getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("jpa/register")
    @ResponseBody()
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody User user) throws JSONException {
        Integer id = userRepository.findUserByLogin(user.getLogin());
        if(id!=null){
            throw new ConflictException("User exist");
        }

        User addedUser = userRepository.save(user);
        JSONObject jsonObject = new JSONObject();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedUser.getId()).toUri();
        jsonObject.put("uri",location);
        jsonObject.put("statuscode",201);
        return ResponseEntity.created(location).body(jsonObject.toString());
    }
    @PostMapping("jpa/login")
    @ResponseBody()
    public Map login(@RequestBody User user){
        System.out.println("user.getLogin() = " + user.getLogin());
        Integer id = userRepository.findUserByLoginAndPassword(user.getLogin(), user.getPassword());
        if(id==null){
            throw new NotFoundException("Incorrect login or password");
        }
        Optional<User> byId= userRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException("id:"+Integer.toString(id) +" not found");
        }

        List<Integer> groupsId = new ArrayList<>();
        for (StudentGroup u:byId.get().getGroupList()) {
            groupsId.add(u.getId());
        }
        Map userResource = new LinkedHashMap();
        List groupResource = new ArrayList();

        userResource.put("id",byId.get().getId());
        userResource.put("login",byId.get().getLogin());
        userResource.put("firstname",byId.get().getFirstname());
        userResource.put("lastname",byId.get().getLastname());

        for (StudentGroup group: byId.get().getGroupList()) {
            Map map = new LinkedHashMap();
            map.put("id",group.getId());
            map.put("specialization",group.getSpecialization());
            map.put("year",group.getYear());
            groupResource.add(map);
        }

        Map map = new HashMap();
        map.put("token",new Token().getKey());
        map.put("user-resource",userResource);
        map.put("groups-resource",groupResource);

        return map;


    }
    @GetMapping("jpa/users/{id}")
    public Resource<User> getUser(@PathVariable int id){
        Optional<User> byId = userRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException("id:"+Integer.toString(id) +" not found");
        }
        Resource<User> resource = new Resource<User>(byId.get());
        ControllerLinkBuilder link = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getAllUsers());
        resource.add(link.withRel("all-users"));
        return resource;
    }
    @DeleteMapping("jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);

    }

    @PostMapping("jpa/users/{user_id}/groups")
    public ResponseEntity<Object> addNewGroup(@PathVariable int user_id, @RequestParam("group_id") Integer groupId) throws URISyntaxException {
        Optional<StudentGroup> sgbyId = studentGroupRepository.findById(groupId);
        if(!sgbyId.isPresent()){
            throw new NotFoundException("group id:"+Integer.toString(groupId) +" not found");
        }
        Optional<User> ubyId = userRepository.findById(user_id);

        if(!ubyId.isPresent()){
            throw new NotFoundException("user id:"+Integer.toString(user_id) +" not found");
        }

        ubyId.get().getGroupList().add(sgbyId.get());
        userRepository.save(ubyId.get());
        Resource resource = new Resource(ubyId.get().getGroupList().toArray());
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedUser.getId()).toUri();
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("jpa/users/{user_id}/groups").buildAndExpand(ubyId.get().getId()).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("jpa/users/{user_id}/groups")
    public List<StudentGroup> getUserGroups(@PathVariable int user_id){
        Optional<User> ubyId = userRepository.findById(user_id);

        if(!ubyId.isPresent()){
            throw new NotFoundException("user id:"+Integer.toString(user_id) +" not found");
        }




        return ubyId.get().getGroupList();
    }

}
