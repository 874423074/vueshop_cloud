package com.markerhub.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.markerhub.core.uitls.OrderStatusUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @TableName app_order
 */
@TableName(value ="app_order")
@Data
public class AppOrder implements Serializable {
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
    private Long userId;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 订单状态：0->待付款；1->待发货；2->待收货；3-已完成；4->已取消；5-申请退款，6->已退款，7->退款失败
     */
    private Integer orderStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付交易号
     */
    private String payTradeNo;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 
     */
    private LocalDateTime completeTime;

    /**
     * 
     */
    private LocalDateTime refundTime;

    /**
     * 收货人
     */
    private String receiverName;

    /**
     * 
     */
    private String receiverAddress;

    /**
     * 
     */
    private String receiverTel;

    /**
     * 
     */
    private String receiverPostalCode;

    /**
     * 
     */
    private String receiverAreaCode;

    /**
     * 
     */
    private String deliveryCompany;

    /**
     * 
     */
    private String deliverySn;

    /**
     * 
     */
    private String adminNote;

    /**
     * 备注
     */
    private String note;

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
    private List<AppOrderItem> orderItems;

    @TableField(exist = false)
    private AppRefund appRefund;

    @TableField(exist = false)
    private String orderStatusStr;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String userAvatar;

    /**
     * 订单状态的中文展示效果
     */
    public String getOrderStatusStr() {
        return OrderStatusUtil.invert(orderStatus);
    }
}
