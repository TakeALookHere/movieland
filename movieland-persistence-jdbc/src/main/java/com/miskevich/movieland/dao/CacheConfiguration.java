//package com.miskevich.movieland.dao;
//
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurer;
//import org.springframework.cache.concurrent.ConcurrentMapCache;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.cache.interceptor.CacheErrorHandler;
//import org.springframework.cache.interceptor.CacheResolver;
//import org.springframework.cache.interceptor.KeyGenerator;
//
//public class CacheConfiguration implements CachingConfigurer {
//    @Override
//    public CacheManager cacheManager() {
//        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(){
//            @Override
//            protected Cache createConcurrentMapCache(final String name){
//                return new ConcurrentMapCache(name, CacheBuilder.)
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public CacheResolver cacheResolver() {
//        return null;
//    }
//
//    @Override
//    public KeyGenerator keyGenerator() {
//        return null;
//    }
//
//    @Override
//    public CacheErrorHandler errorHandler() {
//        return null;
//    }
//}