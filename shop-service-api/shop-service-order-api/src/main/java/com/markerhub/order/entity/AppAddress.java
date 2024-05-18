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
 * @TableName app_address
 */
@TableName(value ="app_address")
@Data
public class AppAddress implements Serializable {
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
    @NotBlank(message = "收货人名称不能为空")
    private String name;

    /**
     * 
     */
    @NotBlank(message = "收货人电话不能为空")
    private String tel;

    /**
     * 
     */
    @NotBlank(message = "收货地址不能为空")
    private String province;

    /**
     * 
     */
    @NotBlank(message = "收货地址不能为空")
    private String city;

    /**
     * 
     */
    @NotBlank(message = "收货地址不能为空")
    private String county;

    /**
     * 
     */
    @NotBlank(message = "收货地址不能为空")
    private String addressDetail;

    /**
     * 
     */
    @NotBlank(message = "地址编码不能为空")
    private String areaCode;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 
     */
    @NotNull(message = "是否默认地址不能为空")
    private Boolean isDefault;

    /**
     * 
     */
    private Boolean isDelete;

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
    private String address;

    public String getAddress() {
        return this.province + this.city + this.county + this.addressDetail;
    }
}