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
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String avatar;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String phone;

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
    private LocalDateTime lastLogin;

    /**
     * 
     */
    private Integer isDelete;

    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    List<SysRole> roles = new ArrayList<>();
}