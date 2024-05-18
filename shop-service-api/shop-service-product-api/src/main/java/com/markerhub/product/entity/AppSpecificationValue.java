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
 * @TableName app_specification_value
 */
@TableName(value ="app_specification_value")
@Data
public class AppSpecificationValue implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long productId;

    /**
     * 
     */
    private String spec;

    /**
     * 
     */
    private String value;

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