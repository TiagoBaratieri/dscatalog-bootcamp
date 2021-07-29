package com.baratieri.dscatalog.services;

import com.baratieri.dscatalog.dto.RoleDTO;
import com.baratieri.dscatalog.dto.UserDTO;
import com.baratieri.dscatalog.dto.UserInsertDTO;
import com.baratieri.dscatalog.entities.Role;
import com.baratieri.dscatalog.entities.User;
import com.baratieri.dscatalog.repositories.RoleRepository;
import com.baratieri.dscatalog.repositories.UserRepository;
import com.baratieri.dscatalog.services.exceptions.DataBaseException;
import com.baratieri.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(PageRequest pageRequest){
        Page<User> list = repository.findAll(pageRequest);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
       User entity = new User();
       copyDtoEntity(dto,entity);
       entity.setPassword(passwordEncoder.encode(dto.getPassword()));
       entity = repository.save(entity);
       return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getOne(id);
            copyDtoEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        }catch (EntityNotFoundException e){
           throw new ResourceNotFoundException("Id not found " +id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoEntity(UserDTO dto, User entity) {
       entity.setFirstName(dto.getFirstName());
       entity.setLastName(dto.getLastName());
       entity.setEmail(dto.getEmail());

        entity.getRoles().clear();

        for(RoleDTO roleDTO: dto.getRoles()){
            Role role = roleRepository.getOne(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

}
