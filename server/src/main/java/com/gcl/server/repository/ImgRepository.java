package com.gcl.server.repository;

import com.gcl.server.bean.Image;
import com.gcl.server.bean.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ImgRepository extends CrudRepository<Image, Integer> {
    List<Image> findByUserId(Integer userId);
    @Transactional
    void deleteByImgName(String imgName);
}