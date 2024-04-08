package com.grigoryev.flowoffate.mapper;

import com.grigoryev.blog.Blog;
import com.grigoryev.flowoffate.tables.pojos.BlogModel;
import org.mapstruct.Mapper;

@Mapper
public interface BlogMapper {

    Blog toBlog(BlogModel blogModel);

}
