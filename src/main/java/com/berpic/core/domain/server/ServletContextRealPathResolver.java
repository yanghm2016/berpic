package com.berpic.core.domain.server;

import com.berpic.core.domain.util.RealPathResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Component
public class ServletContextRealPathResolver implements RealPathResolver,
        ServletContextAware {

    public String get(String path) {
        String realpath = context.getRealPath(path);
        realpath = realpath == null ? getAbsolutePathByClass(path) : realpath;
        return realpath;
    }

    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    private ServletContext context;

    /**
     * 通过类路径来取工程路径
     *
     * @return
     * @throws Exception
     */
    private String getAbsolutePathByClass(String path) {
        String webPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "");
        webPath = webPath.replaceAll("[\\\\\\/]WEB-INF[\\\\\\/]classes[\\\\\\/]?", "/");
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");
        webPath = webPath.replaceAll("%20", " ");

        if (!webPath.matches("^[a-zA-Z]:.*?$")) {
            webPath = "/" + webPath;
        }

        webPath = webPath + "/" + path;
        webPath = webPath.replaceAll("[\\\\\\/]+", "/");
        webPath = webPath.replaceAll("[\\\\\\/]$", "");

        return webPath;
    }

}
