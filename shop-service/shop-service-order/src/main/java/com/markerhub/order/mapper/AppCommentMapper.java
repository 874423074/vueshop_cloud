package com.markerhub.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.order.entity.AppComment;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.markerhub.entity.AppComment
 */
public interface AppCommentMapper extends BaseMapper<AppComment> {

	Page<AppComment> pageByProductId(Page page, @Param("productId") long productId);
}




