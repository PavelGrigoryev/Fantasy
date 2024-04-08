package com.grigoryev.flowoffate.repository;

import com.grigoryev.flowoffate.tables.pojos.BlogModel;
import com.grigoryev.flowoffate.util.HikariConnectionManager;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;

import static com.grigoryev.flowoffate.Tables.BLOG_MODEL;

public class BlogRepository {

    private final DSLContext dslContext;

    public BlogRepository() {
        dslContext = DSL.using(HikariConnectionManager.getConnection());
    }

    public List<BlogModel> findAll() {
        return dslContext.selectFrom(BLOG_MODEL)
                .fetchInto(BlogModel.class);
    }

}
