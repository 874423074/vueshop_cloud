package com.markerhub.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 * @TableName app_sku_stock
 */
@TableName(value ="app_sku_stock")
@Data
public class AppSkuStock implements Serializable {
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
    private BigDecimal price;

    /**
     * 
     */
    private Integer stock;

    /**
     * 
     */
    private String sku;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}