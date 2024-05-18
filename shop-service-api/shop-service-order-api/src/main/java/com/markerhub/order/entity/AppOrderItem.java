package com.markerhub.order.entity;

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
 * @TableName app_order_item
 */
@TableName(value ="app_order_item")
@Data
public class AppOrderItem implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long orderId;

    /**
     * 
     */
    private Long skuId;

    /**
     * 
     */
    private String sku;

    /**
     * 
     */
    private Long categoryId;

    /**
     * 
     */
    private Long productId;

    /**
     * 
     */
    private String productSn;

    /**
     * 
     */
    private String productName;

    /**
     * 
     */
    private String productImage;

    /**
     *
     */
    private BigDecimal price;

    /**
     * 
     */
    private Integer quantity;

    /**
     * 
     */
    private Long commentId;

    /**
     * 
     */
    private LocalDateTime commentTime;

    /**
     * 
     */
    private LocalDateTime created;

    /**
     * 
     */
    private LocalDateTime updated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}