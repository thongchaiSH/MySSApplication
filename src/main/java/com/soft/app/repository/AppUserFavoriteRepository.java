package com.soft.app.repository;

import com.soft.app.entity.app.AppUserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppUserFavoriteRepository extends JpaRepository<AppUserFavorite, Long> {
    List<AppUserFavorite> findByUsername(@Param("username") String username);
}
