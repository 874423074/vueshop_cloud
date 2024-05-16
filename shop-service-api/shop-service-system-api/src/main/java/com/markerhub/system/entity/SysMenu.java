package com.markerhub.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class SysMenu implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    /**
     * 
     */
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 
     */
    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
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

    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}