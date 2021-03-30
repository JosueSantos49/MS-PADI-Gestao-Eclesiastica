
package br.pro.sispro.migreja.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Contexto implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    HibernateUtil.getFabricaDeSessoes().getCurrentSession();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    HibernateUtil.getFabricaDeSessoes().close();
    }
    
}
