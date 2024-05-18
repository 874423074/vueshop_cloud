package com.markerhub.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName app_ad
 */
@TableName(value ="app_ad")
@Data
public class AppAd implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String name;

    /**
     * 广告位置
     */
    private String position;

    /**
     * 
     */
    private String image;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private Boolean isShow;

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
    private Integer sortOrder;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}