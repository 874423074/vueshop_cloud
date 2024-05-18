package com.markerhub.cartItem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 * @TableName app_cart_item
 */
@TableName(value ="app_cart_item")
@Data
public class AppCartItem implements Serializable {
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
    @NotNull(message = "请选择商品规格")
    private Long skuId;

    /**
     * 
     */
    @NotNull(message = "请选择购买商品")
    private Long productId;

    /**
     * 
     */
    @NotNull(message = "请添加购买数量")
    private Integer quantity;

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

    @TableField(exist = false)
    private String productSn;
    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String productImage;

    @TableField(exist = false)
    private Long categoryId;

    @TableField(exist = false)
    private String sku;
    @TableField(exist = false)
    private BigDecimal price;

}