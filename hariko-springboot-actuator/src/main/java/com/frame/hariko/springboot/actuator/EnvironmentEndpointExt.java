package com.frame.hariko.springboot.actuator;

import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查配置中是否有通过uri方式配置账号密码的敏感配置
 * 形如:
 *   protocol://username:password@host:port
 *   替换为
 *   protocol://username:******@host:port
 */
public class EnvironmentEndpointExt extends EnvironmentEndpoint {
    private static final String REGEX = "//(.*):.*@";
    private static Pattern PATTERN = Pattern.compile(REGEX);

    public EnvironmentEndpointExt(Environment environment) {
        super(environment);
    }

    @Override
    public Object sanitize(String name, Object object) {
        if(Objects.nonNull(object) && object instanceof String){
            String value = (String) object;
            Matcher matcher = PATTERN.matcher(value);
            if(matcher.find()){
                return value.replaceAll(REGEX, "//"+matcher.group(1)+":******@");
            }
        }
        return super.sanitize(name, object);
    }
}