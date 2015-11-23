//package com.berpic.core.config;
//
//import com.bersolr.core.domain.util.Constants;
//import com.bersolr.core.domain.util.EnumUtils;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author 任小斌
// * @version V1.0
// * @Title: SingletonFactory.java
// * @Package com.bersolr.core.config
// * @Description: TODO(单例模式，初始化索引连接)
// * @date Aug 21, 2015 2:44:01 PM
// */
//public class SingletonFactory {
//
//    // 初始化单例模式
//    private static class SingletBuildFactory {
//        // PC 端商品
//        private static final HttpSolrServer
//                INSTANCE_CORE1 = new HttpSolrServer(EnumUtils._SEO_GOODSPC_.getEnumName());
//        // 团购商品
//        private static final HttpSolrServer
//                INSTANCE_CORE2 = new HttpSolrServer(EnumUtils._SEO_GROUP_.getEnumName());
//        // 移动端商品
//        private static final HttpSolrServer
//                INSTANCE_CORE3 = new HttpSolrServer(EnumUtils._SEO_GOODSAPP_.getEnumName());
//        // 三位商城智能提示
//        private static final HttpSolrServer
//                INSTANCE_SUGGEST = new HttpSolrServer(EnumUtils._SEO_SUGGESTS_.getEnumName());
//        // 门户搜索
//        private static final HttpSolrServer
//                INSTANCE_SITE = new HttpSolrServer(EnumUtils._SEO_SITE_.getEnumName());
//        // 门户智能提示
//        private static final HttpSolrServer
//                INSTANCE_SUGGESTS = new HttpSolrServer(EnumUtils._SEO_SITE_SUGGEST_.getEnumName());
//    }
//
//    // 防止泄漏
//    private SingletonFactory() {
//    }
//
//    // 广播调用服务对象
//    public static final HttpSolrServer getInstance(String type) {
//        switch (type) {
//            case Constants._SEO_SOLR_1:
//                return SingletBuildFactory.INSTANCE_CORE1;
//            case Constants._SEO_SOLR_2:
//                return SingletBuildFactory.INSTANCE_CORE2;
//            case Constants._SEO_SOLR_3:
//                return SingletBuildFactory.INSTANCE_CORE3;
//            case Constants._SEO_SOLR_4:
//                return SingletBuildFactory.INSTANCE_SUGGEST;
//            case Constants._SEO_SOLR_5:
//                return SingletBuildFactory.INSTANCE_SITE;
//            case Constants._SEO_SOLR_6:
//                return SingletBuildFactory.INSTANCE_SUGGESTS;
//            default:
//                break;
//        }
//        return null;
//    }
//
//}
