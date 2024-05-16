package com.markerhub.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole implements Serializable {
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
     * 
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

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
    List<Long> menuIds = new ArrayList<>();
}