package su.dru.ignite.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import su.dru.ignite.domain.Cell;
import su.dru.ignite.domain.CellToNumberRelation;
import su.dru.ignite.domain.ClientProfile;

/**
 * @author Andrey Vorobyov
 */
@Configuration
public class IgniteProvider {
    private final Ignite ignite = Ignition.start("ignite.xml");

    @Bean
    @ConditionalOnClass(name = "org.apache.ignite.Ignite")
    public Ignite getIgnite() {
        return ignite;
    }

    @Bean
    @Qualifier(Cell.CACHE_NAME)
    public IgniteCache<Long, Cell> getCellCache() {
        CacheConfiguration<Long, Cell> cellCache = new CacheConfiguration<>(Cell.CACHE_NAME);
        cellCache.setCacheMode(CacheMode.REPLICATED);
        cellCache.setIndexedTypes(Long.class, Cell.class);

        return ignite.getOrCreateCache(cellCache);
    }

    @Bean
    @Qualifier(ClientProfile.CACHE_NAME)
    public IgniteCache<Long, ClientProfile> getClientProfile() {
        CacheConfiguration<Long, ClientProfile> clientProfileCache = new CacheConfiguration<>(ClientProfile.CACHE_NAME);
        clientProfileCache.setCacheMode(CacheMode.REPLICATED);
        clientProfileCache.setIndexedTypes(Long.class, ClientProfile.class);

        return ignite.getOrCreateCache(clientProfileCache);
    }

    @Bean
    @Qualifier(CellToNumberRelation.CACHE_NAME)
    public IgniteCache<Long, CellToNumberRelation> getCellToNumberRelation() {
        CacheConfiguration<Long, CellToNumberRelation> cellToNumberCache = new CacheConfiguration<>(CellToNumberRelation.CACHE_NAME);
        cellToNumberCache.setCacheMode(CacheMode.REPLICATED);
        cellToNumberCache.setIndexedTypes(Long.class, CellToNumberRelation.class);

        return ignite.getOrCreateCache(cellToNumberCache);
    }
}
