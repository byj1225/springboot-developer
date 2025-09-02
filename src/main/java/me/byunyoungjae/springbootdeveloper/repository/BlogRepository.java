package me.byunyoungjae.springbootdeveloper.repository;

import me.byunyoungjae.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
