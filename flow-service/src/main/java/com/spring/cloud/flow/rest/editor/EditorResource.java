//package com.spring.cloud.flow.rest.editor;
//
//import Group;
//import GroupRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * Created by CDZ on 2018/9/14.
// */
//@RestController
//public class EditorResource {
//
//    @Autowired
//    private GroupRepository groupRepository;
//    @GetMapping(value = "/app/rest/editor-groups")
//    public List<Group> editorGroups(){
//        return groupRepository.findAll();
//    }
//}
