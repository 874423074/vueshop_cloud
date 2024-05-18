package com.markerhub.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName app_comment
 */
@TableName(value ="app_comment")
@Data
public class AppComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long productId;

    /**
     * 
     */
    @NotNull(message = "评论商品Id不能为空")
    private Long orderItemId;

    /**
     * 
     */
    private Long orderId;

    /**
     * 
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 
     */
    @NotNull(message = "评论分数不能为空")
    private Integer score;

    /**
     * 
     */
    private String images;

    /**
     * 
     */
    private String reply;

    /**
     * 
     */
    private LocalDateTime created;

    /**
     * 
     */
    private LocalDateTime updated;

    /**
     * 
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String userAvatar;
}