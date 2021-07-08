package com.lwj.demo.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class PageVo<T> implements Serializable {

    /**
     * 数量
     */
    private List<T> records;
    /**
     * sql 条件
     */
    private Map<String, Object> condition = new HashMap<>();
    /**
     * 开始页数
     */
    private int current = 0;
    /**
     * 每页的数据量
     */
    private int size = 10;

    /**
     * 用来计算起始的pageNum
     */
    private Integer startLimit;
    /**
     * 总数
     */
    private long total = 0;


    /**
     * 获取分业的filter  如果传入的filter为空。会自动生成一个，初始容量为 5 的map
     *
     * @param pageVo
     * @param filter
     * @return
     */
    public static Map<String, Object> getPageFilter(PageVo pageVo, Map<String, Object> filter) {
        if (filter.isEmpty()) {
            filter = new HashMap<>(5);
        }
        pageVo = initPage(pageVo);
        filter.put("start", pageVo.getCurrent() * pageVo.getSize());
        filter.put("size", pageVo.getSize());
        return filter;
    }

    /**
     * pageVo转换成mybatis-plus的分页参数
     *
     * @param pageVo
     * @return
     */
    public static Page coverPage(PageVo pageVo) {
        if (pageVo != null) {
            return new Page(pageVo.getCurrent(), pageVo.getSize());
        }
        return null;
    }


    /**
     * 初始化分页
     *
     * @param pageVo
     * @return
     */
    public static PageVo initPage(PageVo pageVo) {
        int maxSize = 40;
        if (pageVo.getCurrent() < 0) {
            pageVo.setCurrent(0);
        } else if (pageVo.getCurrent() > 0) {
            pageVo.setCurrent(pageVo.getCurrent() - 1);
        }
        if (pageVo.getSize() < 0) {
            pageVo.setSize(10);
        } else if (pageVo.getSize() > maxSize) {
            pageVo.setSize(maxSize);
        }
        return pageVo;
    }


}
