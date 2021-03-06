package com.frame.hariko.springboot.web.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JacksonProperties jacksonProperties;

    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.applicationContext(this.applicationContext);
        if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
            builder.serializationInclusion(
                    this.jacksonProperties.getDefaultPropertyInclusion());
        }
        if (this.jacksonProperties.getTimeZone() != null) {
            builder.timeZone(this.jacksonProperties.getTimeZone());
        }else{
            //当未指定配置时区时,获取JVM默认时区
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        }
        configureFeatures(builder, this.jacksonProperties.getDeserialization());
        configureFeatures(builder, this.jacksonProperties.getSerialization());
        configureFeatures(builder, this.jacksonProperties.getMapper());
        configureFeatures(builder, this.jacksonProperties.getParser());
        configureFeatures(builder, this.jacksonProperties.getGenerator());
        configureDateFormat(builder);
        configurePropertyNamingStrategy(builder);
        configureModules(builder);
        configureLocale(builder);
        return builder;
    }

    private void configureFeatures(Jackson2ObjectMapperBuilder builder,
                                   Map<?, Boolean> features) {
        for (Map.Entry<?, Boolean> entry : features.entrySet()) {
            if (entry.getValue() != null && entry.getValue()) {
                builder.featuresToEnable(entry.getKey());
            }
            else {
                builder.featuresToDisable(entry.getKey());
            }
        }
    }

    private void configureDateFormat(Jackson2ObjectMapperBuilder builder) {
        // We support a fully qualified class name extending DateFormat or a date
        // pattern string value
        String dateFormat = this.jacksonProperties.getDateFormat();
        if (dateFormat != null) {
            try {
                Class<?> dateFormatClass = ClassUtils.forName(dateFormat, null);
                builder.dateFormat(
                        (DateFormat) BeanUtils.instantiateClass(dateFormatClass));
            }
            catch (ClassNotFoundException ex) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                // Since Jackson 2.6.3 we always need to set a TimeZone (see gh-4170)
                // If none in our properties fallback to the Jackson's default
                TimeZone timeZone = this.jacksonProperties.getTimeZone();
                if (timeZone == null) {
                    timeZone = TimeZone.getTimeZone(ZoneId.systemDefault());
                }
                simpleDateFormat.setTimeZone(timeZone);
                builder.dateFormat(simpleDateFormat);
            }
        }
    }

    private void configurePropertyNamingStrategy(
            Jackson2ObjectMapperBuilder builder) {
        // We support a fully qualified class name extending Jackson's
        // PropertyNamingStrategy or a string value corresponding to the constant
        // names in PropertyNamingStrategy which hold default provided implementations
        String strategy = this.jacksonProperties.getPropertyNamingStrategy();
        if (strategy != null) {
            try {
                configurePropertyNamingStrategyClass(builder,
                        ClassUtils.forName(strategy, null));
            }
            catch (ClassNotFoundException ex) {
                configurePropertyNamingStrategyField(builder, strategy);
            }
        }
    }

    private void configurePropertyNamingStrategyClass(
            Jackson2ObjectMapperBuilder builder,
            Class<?> propertyNamingStrategyClass) {
        builder.propertyNamingStrategy((PropertyNamingStrategy) BeanUtils
                .instantiateClass(propertyNamingStrategyClass));
    }

    private void configurePropertyNamingStrategyField(
            Jackson2ObjectMapperBuilder builder, String fieldName) {
        // Find the field (this way we automatically support new constants
        // that may be added by Jackson in the future)
        Field field = ReflectionUtils.findField(PropertyNamingStrategy.class,
                fieldName, PropertyNamingStrategy.class);
        Assert.notNull(field, "Constant named '" + fieldName + "' not found on "
                + PropertyNamingStrategy.class.getName());
        try {
            builder.propertyNamingStrategy((PropertyNamingStrategy) field.get(null));
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void configureModules(Jackson2ObjectMapperBuilder builder) {
        Collection<Module> moduleBeans = getBeans(this.applicationContext,
                Module.class);
        builder.modulesToInstall(moduleBeans.toArray(new Module[moduleBeans.size()]));
    }

    private void configureLocale(Jackson2ObjectMapperBuilder builder) {
        Locale locale = this.jacksonProperties.getLocale();
        if (locale != null) {
            builder.locale(locale);
        }
    }

    private static <T> Collection<T> getBeans(ListableBeanFactory beanFactory,
                                              Class<T> type) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, type)
                .values();
    }
}
