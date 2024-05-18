package com.markerhub.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.markerhub.core.uitls.RefundStatusUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 * @TableName app_refund
 */
@TableName(value ="app_refund")
@Data
public class AppRefund implements Serializable {
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
    @NotNull(message = "退款订单不能为空")
    private Long orderId;

    /**
     * 
     */
    private String orderSn;

    /**
     * 
     */
    private String sn;

    /**
     * 
     */
    private BigDecimal refundAmount;

    /**
     * 退货方式，0-自行寄回，1-上门取货
     */
    @NotBlank(message = "退款方式不能为空")
    private String method;

    /**
     * 退款理由
     */
    @NotBlank(message = "退款理由不能为空")
    private String reason;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String images;

    /**
     * 
     */
    private Long operatorId;

    /**
     * 
     */
    private String operateRemark;

    /**
     * 
     */
    private LocalDateTime operateTime;

    /**
     * 
     */
    private String receiptRemark;

    /**
     * 申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝
     */
    private Integer refundStatus;

    @TableField(exist = false)
    private String refundStatusStr;

    public String getRefundStatusStr() {
        return RefundStatusUtil.invert(this.refundStatus);
    }


    /**
     * 
     */
    private LocalDateTime refundTime;

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
    private String username;

    @TableField(exist = false)
    private String userAvatar;

}
