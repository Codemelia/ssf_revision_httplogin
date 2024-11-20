package vttp.ssf.day13_httplogin.configurations;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha generateCaptcha() {
        
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // Basic CAPTCHA settings
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        properties.setProperty("kaptcha.image.width", "250");
        properties.setProperty("kaptcha.image.height", "75");
        properties.setProperty("kaptcha.textproducer.font.size", "50");
        properties.setProperty("kaptcha.session.key", "captchaCode");
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.noise.color", "black");

        defaultKaptcha.setConfig(new Config(properties));

        return defaultKaptcha;
    }
    
}
