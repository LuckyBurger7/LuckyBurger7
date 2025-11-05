package org.example.luckyburger.common.cache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@Component
public class CacheStatsPrinter {
    
    private final CacheManager cacheManager;

    public CacheStatsPrinter(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    public void printCacheStats(){
        Cache cache = cacheManager.getCache("getTopTenShopTotalSales");
        if (cache != null){
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            CacheStats stats = caffeineCache.getNativeCache().stats();
            System.out.println("Cache hit count: " + stats.hitCount());
            System.out.println("Cache miss count: " + stats.missCount());
            System.out.println("Cache hit rate: " + stats.hitRate());
        }
    }
}
