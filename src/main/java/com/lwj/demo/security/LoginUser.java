package com.lwj.demo.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lwj
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1158761213706993647L;
    private String username;
    private String password;
}