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
 * @TableName app_specification
 */
@TableName(value ="app_specification")
@Data
public class AppSpecification implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private String name;

    /**
     * 可选项
     */
    private String options;

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