package com.markerhub.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @TableName app_product
 */
@TableName(value ="app_product")
@Data
public class AppProduct implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String sn;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private String image;

    /**
     * 
     */
    private String pics;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private BigDecimal cost;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 
     */
    private String keywords;

    /**
     * 
     */
    private String detail;

    /**
     * 
     */
    private Boolean isOnSale;

    /**
     * 
     */
    private Boolean isNew;

    /**
     * 
     */
    private Boolean isTop;

    /**
     * 
     */
    private Boolean isHot;

    /**
     * 
     */
    private Integer sortOrder;

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
    private Boolean isDelete;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 评论数量
     */
    private Integer comments;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    List<AppSpecificationValue> specifications;

    @TableField(exist = false)
    List<AppSkuStock> skuStocks;

    @TableField(exist = false)
    private String categoryName;
}