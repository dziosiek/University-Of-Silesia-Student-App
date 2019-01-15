package com.api.us.restapi.student_group;

import com.api.us.restapi.events.Event;
import com.api.us.restapi.events.EventRepository;
import com.api.us.restapi.my.exceptions.NotFoundException;
import com.api.us.restapi.timetable.Timetable;
import com.api.us.restapi.timetable.TimetableRepository;
import com.api.us.restapi.user.User;
import com.api.us.restapi.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentGroupRestController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;
    @Autowired
    EventRepository eventRepository;

    @Autowired
    TimetableRepository timetableRepository;

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
        if(!byId.isPresent())
            throw new NotFoundException("id group:"+Integer.toString(id) +" not found");
        return byId.get().getUserList();
    }

    @GetMapping("jpa/student-groups/{id}/events")
    public List<Event> getEventsByGroupId(@PathVariable Integer id){
        Optional<StudentGroup> byId= studentGroupRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException("id:"+id+" not found");
        }
        return byId.get().getEvents();
    }

    @PostMapping("jpa/student-groups/{groupId}/events")
    public ResponseEntity<Object> addEvent(@PathVariable Integer groupId,
                                           @RequestBody Event event,
                                           @RequestParam("user_id") Integer userId){
        Optional<StudentGroup> studentGroupById= studentGroupRepository.findById(groupId);
        Optional<User> userById = userRepository.findById(userId);
        if(!studentGroupById.isPresent())
            throw new NotFoundException("group id:"+groupId+" not found");
        if(!userById.isPresent())
            throw new NotFoundException("user id:"+userById+" not found");
        event.setGroup(studentGroupById.get());
        event.setUser(userById.get());

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            Date date1 = sdf.parse(event.getDate().toString());
//            event.setDate(date1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Event eventSaved = eventRepository.save(event);
        Resource<Event> resource = new Resource<>(eventSaved);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventSaved.getId()).toUri();
        return ResponseEntity.created(location).body(eventSaved);
    }

    @GetMapping("jpa/student-groups/{groupId}/events/{eventId}")
    public Event getEvent(@PathVariable Integer groupId, @PathVariable Integer eventId) {
        Optional<StudentGroup> studentGroupById = studentGroupRepository.findById(groupId);
        Optional<Event> eventById = eventRepository.findById(eventId);
        if (!studentGroupById.isPresent())
            throw new NotFoundException("group id:" + groupId + " not found");
        if (!eventById.isPresent())
            throw new NotFoundException("event id:" + eventId + " not found");
        return eventById.get();
    }

    @DeleteMapping("jpa/student-groups/{groupId}/events/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Integer groupId, @PathVariable Integer eventId){
        Optional<Event> eventById = eventRepository.findById(eventId);
        Optional<StudentGroup> groupById= studentGroupRepository.findById(groupId);
        if(!eventById.isPresent()) {
            throw new NotFoundException("event id:" + eventId + " not found");
        }
        if(!groupById.isPresent())
            throw new NotFoundException("group id:"+groupId+" not found");
        eventRepository.delete(eventById.get());
        return ResponseEntity.noContent().build();
    }
    @PostMapping("jpa/student-groups/{groupId}/timetables")
    public ResponseEntity<Object> addTimetable(@PathVariable Integer groupId,
                                               @RequestBody Timetable timetable){
        Optional<StudentGroup> studentGroupById = studentGroupRepository.findById(groupId);
        if(!studentGroupById.isPresent())
            throw new NotFoundException("group id:"+groupId+" not found");

        timetable.setGroup(studentGroupRepository.getOne(groupId));
        Timetable saved = timetableRepository.save(timetable);
        Resource<Timetable> resource = new Resource<>(saved);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }
    @GetMapping("jpa/student-groups/{groupId}/timetables")
    public ResponseEntity<Object> getTimetables(){
        return ResponseEntity.ok(timetableRepository.findAll());
    }
    @GetMapping("jpa/student-groups/{groupId}/timetables/{timetableId}")
    public ResponseEntity<Object> getTimetable(@PathVariable Integer groupId,
                                               @PathVariable Integer timetableId){
        Optional<StudentGroup> group = studentGroupRepository.findById(groupId);
        Optional<Timetable> timetable = timetableRepository.findById(timetableId);
        if (!group.isPresent())
            throw new NotFoundException("group id:" + groupId + " not found");
        if (!timetable.isPresent())
            throw new NotFoundException("timetable id:" + timetableId+ " not found");

        return ResponseEntity.ok().body(timetable.get());
    }
    @DeleteMapping("jpa/student-groups/{groupId}/timetables/{timetableId}")
    public ResponseEntity<Object> deleteTimetable(@PathVariable Integer groupId,
                                               @PathVariable Integer timetableId){
        Optional<StudentGroup> group = studentGroupRepository.findById(groupId);
        Optional<Timetable> timetable = timetableRepository.findById(timetableId);
        if (!group.isPresent())
            throw new NotFoundException("group id:" + groupId + " not found");
        if (!timetable.isPresent())
            throw new NotFoundException("timetable id:" + timetableId+ " not found");

        timetableRepository.delete(timetable.get());
        return ResponseEntity.noContent().build();
    }

    @Scheduled(fixedRate = 500)
    public void deletePastEvents() {
    }
}
