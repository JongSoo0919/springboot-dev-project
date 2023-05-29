package com.study.projectboard.repository;

import com.study.projectboard.config.JpaConfig;
import com.study.projectboard.domain.Article;
import com.study.projectboard.domain.ArticleComment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired
            ArticleRepository articleRepository,
            @Autowired
            ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenMocksFine(){
        //given

        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);

        List<ArticleComment> articleComments = articleCommentRepository.findAll();

        assertThat(articleComments)
                .isNotNull()
                .hasSize(0);
    }
    @DisplayName("insert test")
    @Test
    void givenTestData_whenInserting_thenMocksFine(){
        //given
        long previousCount = articleRepository.count();

        //when
        Article savedArticle = articleRepository.save(Article.of("new article", "new Content", " #Spring"));
        System.out.println(savedArticle.toString());

        //then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);
    }

    @DisplayName("update test")
    @Test
    void givenTestData_whenUpdating_thenMocksFine(){
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatingHashtag = "#Springboot";
        article.setHashtag(updatingHashtag);

        // when
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatingHashtag);
    }

    @DisplayName("delete test")
    @Test
    void givenTestData_whenDeleting_thenMocksFine(){
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count())
                .isEqualTo(previousArticleCount - 1);
    }
}