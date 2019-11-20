/**
 * Copyright (C), 2018-2019, 潮运科技
 * FileName: BaseAdminController
 * Author:   MrYe
 * Date:     2019/4/10 4:19 PM
 * Description: 基本类型
 * History:
 * <author>                <time>          <version>          <desc>
 * MrYe        2019/4/10 4:19 PM         1.0.0         〈基本类型〉
 */
package com.jixunkeji.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jixunkeji.page.LayuiPageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class BaseAdminController {

    private Logger logger = LoggerFactory.getLogger(BaseAdminController.class);


    /**
     * 获取分页数据
     *
     * @return
     */
    protected Page getPage() {
        Page page = LayuiPageFactory.defaultPage();
        return page;
    }


}