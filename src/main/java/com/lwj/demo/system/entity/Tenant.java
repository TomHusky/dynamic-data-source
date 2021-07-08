package com.lwj.demo.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * saas租户
 * </p>
 *
 * @author lwj
 * @since 2020-09-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("sys_tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 5757214508891529086L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "tenantName不能为空")
    private String tenantName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    @NotBlank(message = "phone不能为空")
    private String phone;

    /**
     * logo地址
     */
    private String logoUrl;

    /**
     * 状态
     */
    private Boolean isDel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
