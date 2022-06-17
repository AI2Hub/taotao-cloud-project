package com.taotao.cloud.auth.biz.idserver.service;


import com.taotao.cloud.auth.biz.idserver.entity.Menu;
import java.util.List;

/**
 * The interface Menu service.
 *
 * @author felord.cn
 * @since 1.0.0
 */
public interface MenuService {


    /**
     * Save menu.
     *
     * @param menu the menu
     * @return the menu
     */
    Menu save(Menu menu);

    /**
     * 修改 菜单
     *
     * @param menu 菜单
     * @return the menu
     */
    Menu update(Menu menu);

    /**
     * 通过 id 查询 菜单
     *
     * @param id id
     * @return 菜单 menu
     */
    Menu findById(String id);

    /**
     * 菜单列表
     *
     * @return list list
     */
    List<Menu> findAll();

    /**
     * Find by root list.
     *
     * @return the list
     */
    List<Menu> findByRoot();

    /**
     * Parents list.
     *
     * @return the list
     */
    List<Menu> parents();
}
