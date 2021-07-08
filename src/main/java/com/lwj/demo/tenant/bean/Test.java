package com.lwj.demo.tenant.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系統用戶表
 * </p>
 *
 * @author lvbao
 * @since 2020-09-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_test")
public class Test implements Serializable {

    private static final long serialVersionUID = -6367853010469956128L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    private String name;

}
