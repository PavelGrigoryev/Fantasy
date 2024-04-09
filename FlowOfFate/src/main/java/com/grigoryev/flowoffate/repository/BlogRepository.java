package com.grigoryev.flowoffate.repository;

import com.grigoryev.flowoffate.tables.pojos.BlogModel;
import com.grigoryev.flowoffate.util.HikariConnectionManager;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Optional;

import static com.grigoryev.flowoffate.Tables.BLOG_MODEL;

public class BlogRepository {

    private final DSLContext dslContext;

    public BlogRepository() {
        dslContext = DSL.using(HikariConnectionManager.getConnection());
    }

    public Optional<BlogModel> findById(String id) {
        return dslContext.selectFrom(BLOG_MODEL)
                .where(BLOG_MODEL.ID.eq(id))
                .fetchOptional()
                .map(blogModelRecord -> blogModelRecord.into(BlogModel.class));
    }

    public List<BlogModel> findAll() {
        return dslContext.selectFrom(BLOG_MODEL)
                .fetchInto(BlogModel.class);
    }

}
