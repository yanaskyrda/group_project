package com.groupproject.microservice.order.clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

@Component
public class CatalogClient {

    private final Logger log = LoggerFactory.getLogger(CatalogClient.class);

    private final static String CATALOG_TEMPLATE = "http://%s:%s/catalog/";

    public static class ItemPagedResources extends PagedModel<PaintingItem> {

    }

    private final RestTemplate restTemplate;
    private final String catalogServiceHost;
    private final long catalogServicePort;
    private final boolean useRibbon;
    private LoadBalancerClient loadBalancer;
    private Collection<PaintingItem> itemsCache = null;

    @Autowired
    public CatalogClient(
            @Value("${catalog.service.host:catalog}") String catalogServiceHost,
            @Value("${catalog.service.port:8080}") long catalogServicePort,
            @Value("${ribbon.eureka.enabled:false}") boolean useRibbon) {
        super();
        this.restTemplate = getRestTemplate();
        this.catalogServiceHost = catalogServiceHost;
        this.catalogServicePort = catalogServicePort;
        this.useRibbon = useRibbon;
    }

    @Autowired(required = false)
    public void setLoadBalancer(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    protected RestTemplate getRestTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaTypes.HAL_JSON));
        converter.setObjectMapper(mapper);

        return new RestTemplate(
                Collections.<HttpMessageConverter<?>>singletonList(converter));
    }

    @HystrixCommand(fallbackMethod = "priceCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public double price(long itemId) {
        return getOne(itemId).getPrice();
    }

    @SuppressWarnings("unused")
    public double priceCache(long itemId) {
        return getOneCache(itemId).getPrice();
    }

    @HystrixCommand(fallbackMethod = "getItemsCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public Collection<PaintingItem> findAll() {
        PagedModel<PaintingItem> pagedResources = restTemplate.getForObject(
                catalogURL(), ItemPagedResources.class);
        assert pagedResources != null;
        this.itemsCache = pagedResources.getContent();
        return pagedResources.getContent();
    }

    @SuppressWarnings("unused")
    private Collection<PaintingItem> getItemsCache() {
        return itemsCache;
    }

    private String catalogURL() {
        String url;
        if (useRibbon) {
            ServiceInstance instance = loadBalancer.choose("CATALOG");
            url = String.format(CATALOG_TEMPLATE, instance.getHost(), instance.getPort());
        } else {
            url = String.format(CATALOG_TEMPLATE, catalogServiceHost, catalogServicePort);
        }
        log.trace("Catalog: URL {} ", url);
        return url;
    }

    @HystrixCommand(fallbackMethod = "getOneCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public PaintingItem getOne(long itemId) {
        return restTemplate.getForObject(catalogURL() + itemId, PaintingItem.class);
    }

    public PaintingItem getOneCache(long itemId) {
        return itemsCache.stream()
                .filter(i -> (i.getItemId() == itemId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
