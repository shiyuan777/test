
package com.jixunkeji.page;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jixunkeji.utils.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Layui Table默认的分页参数创建
 */
public class LayuiPageFactory {

    /**
     * 获取layui table的分页参数
     */
    public static Page defaultPage() {
        HttpServletRequest request = HttpUtil.getRequest();

        //当前页
        int current = Integer.valueOf(request.getParameter("_pageNo"));

        //条数
        int size = Integer.valueOf(request.getParameter("_pageSize"));

        return new Page(current, size);
    }

}
