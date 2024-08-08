package com.stitch.user.service.role;

import com.stitch.user.model.entity.Permission;
import com.stitch.user.model.entity.Role;
import com.stitch.user.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;






@Component
public class RoleManager implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger log = LoggerFactory.getLogger(RoleManager.class);

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
//
//        createRoleIfNotExist("CUSTOMER");
//        createRoleIfNotExist("VENDOR");

        alreadySetup = true;
    }

//    @Transactional
//    Permission createPrivilegeIfNotFound(String name, String description, String category) {
//
//        Optional<Permission> privilege = permissionRepository.findByName(name);
//        if (privilege.isEmpty()) {
//            Permission permission = new Permission(name, description, category);
//            return permissionRepository.save(permission);
//        }else {
//            return privilege.get();
//        }
//    }
//
//    @Transactional
//    Role createRoleIfNotFound(String name, String description, Collection<Permission> permissions) {
//
//        Collection<Permission> permissionList = permissionRepository.saveAll(permissions);
//        Optional<Role> roleExists = roleRepository.findByName(name);
//        if (roleExists.isEmpty()) {
//            Role role = new Role(name, description);
//            role.setPermissions(permissionList);
//            return roleRepository.save(role);
//        }else {
//            Role role = roleExists.get();
////            role.setPermissions(permissionList);
//            log.info("permissionList : {}", permissionList);
//            return roleRepository.save(role);
//        }
//
//    }

//    @Transactional
//    Role createRoleIfNotExist(String roleName){
//        Optional<Role> roleExist = roleRepository.findRoleByName(roleName);
//        if (roleExist.isEmpty()) {
//            Role role = new Role();
//            role.setName(roleName);
//            role.setDescription(roleName);
//            return roleRepository.save(role);
//        }else {
//            return roleExist.get();
//        }
//    }
}



