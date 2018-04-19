package com.gcl.server.repository;

import com.gcl.server.bean.News;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NewsRepository extends CrudRepository<News, Integer> {

    // 根据用户id返回news
    List<News> findByUserId(Integer userId);

    // 用户推荐, 随机选择
    @Query(value = "SELECT * FROM news " +
            "WHERE id >= (SELECT FLOOR(MAX(id) * RAND()) FROM news) ORDER BY id LIMIT 10",
        nativeQuery = true)
    List<News> custom();

    @Query(value = "select * from news where tag = ?1 limit ?2, ?3", nativeQuery = true)
    List<News> findByTagAndPage(String tag, Integer pageStart, Integer pageEnd);
}