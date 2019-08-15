package com.fulaan.listener;

import com.fulaan.utils.KeyWordFilterUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by wang_xinxin on 2015/8/3.
 */
public class KeyWordFilterListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        KeyWordFilterUtil.init();
    }
}
