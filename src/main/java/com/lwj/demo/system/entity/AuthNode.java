package com.lwj.demo.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 节点表
 * </p>
 *
 * @author lvbao
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_auth_node")
public class AuthNode implements Serializable {

    private static final long serialVersionUID = -1622126583445395333L;

    @NotNull(message = "id不能为空", groups = {Delete.class, Get.class, Edit.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 节点名称
     */
    @NotBlank(message = "name不能为空", groups = {Add.class})
    private String name;

    /**
     * 路径
     */
    @NotBlank(message = "path不能为空", groups = {Add.class})
    private String path;

    /**
     * 排序
     */
    private Integer listOrder;

    /**
     * 状态 1=正常 2=停用 -1=软删除
     */
    private Boolean isDel;

    /**
     * 子menu
     */
    @TableField(exist = false)
    private List<AuthNode> children;

    /**
     * 是否选择
     */
    @TableField(exist = false)
    private boolean checked;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public interface Add {

    }

    public interface Edit {

    }

    public interface Delete {

    }

    public interface Get {

    }
}
