//package com.berpic.core.config;
//
//import com.bersolr.core.domain.util.ConfigUtils;
//import com.bersolr.core.domain.util.Constants;
//import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.data.solr.core.SolrTemplate;
//import org.springframework.data.solr.repository.config.EnableSolrRepositories;
//import org.springframework.data.solr.server.support.HttpSolrServerFactory;
//import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;
//
//import javax.annotation.Resource;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author 任小斌
// * @version V1.0
// * @Title: SearchSolrControll.java
// * @Package com.bersolr.core.config
// * @Description: TODO(创建搜索服务，根据业务自动识别选取Cores对应的服务)
// * @date Aug 21, 2015 2:44:01 PM
// */
//@Configuration
//public class SolrServerFactory {
//    public static Map<String, HttpSolrServer> servers;
//
//    static {
//        servers = new HashMap<String, HttpSolrServer>();
//        //静态加载所有的solrServer
//        String[] cores = ConfigUtils.getPropertyValue(Constants.CORES).split(Constants.COMMA);
//        String base_url = ConfigUtils.getPropertyValue(Constants.BASE_URL);
//        for (String core : cores) {
//            String url;
//            if (base_url.endsWith(Constants.SLASH)) {
//                url = base_url + core;
//            } else {
//                url = base_url + Constants.SLASH + core;
//            }
//            HttpSolrServer server = new HttpSolrServer(url);
//            servers.put(core, server);
//        }
//    }
//
//    @Bean
//    public Map<String, HttpSolrServer> getServers() {
//        return servers;
//    }
//
//    /**
//     * 创建 Cores 服务
//     *
//     * @param collection 自动识别搜索实例名称（collection1,collection2）
//     * @return HttpSolrServer
//     */
//    public HttpSolrServer newInstance(String collection) {
//        if (servers.containsKey(collection))
//            return servers.get(collection);
//        String base_url = ConfigUtils.getPropertyValue(Constants.BASE_URL);
//        if (base_url.endsWith(Constants.SLASH))
//            base_url = base_url + collection;
//        else
//            base_url = base_url + Constants.SLASH + collection;
//        return servers.put(collection, new HttpSolrServer(base_url));
//    }
//}
