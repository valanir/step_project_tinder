import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.watermelon.Profile;
import org.watermelon.servlets.UserServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        config.setClassForTemplateLoading(UserServlet.class.getClass(), "/html");
        TemplateLoader ldr = new ClassTemplateLoader(test.class, "/html");
        config.setTemplateLoader(ldr);
        Map<String, Object> data = new HashMap<>();
        Profile showProfile = new Profile(12, "test", "test");
        data.put("username", showProfile.getNickName());
        data.put("image", showProfile.getPhotoSource());
        try {
            System.out.println(config.getTemplate("like-page.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
