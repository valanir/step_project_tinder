package org.watermelon.servlets;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.watermelon.Profile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DynamicServlet extends HttpServlet {

    private List<String> userLog = new ArrayList<>();
    private static Map<Integer, Profile> userProfiles;

    private Integer userIdCounter = 1;

    public void initUserProfiles() {
        userProfiles = new HashMap<>();
        userProfiles.put(1, new Profile(1,"UnknownBeauty", "https://i.pinimg.com/originals/bf/1e/b7/bf1eb795b5f269e3ca7a833f954d1c85.jpg"));
        userProfiles.put(2, new Profile(2, "Witch", "https://vjoy.cc/wp-content/uploads/2019/07/2-48.jpg"));
        userProfiles.put(3, new Profile(3, "HelloKitty", "https://vjoy.cc/wp-content/uploads/2021/02/2f7147400505b7341a3d2f1b913f55b9747b06fe16cc3a14c05b0814a3c42b80.jpg"));
        userProfiles.put(4, new Profile(4, "LifeDestroyer", "https://proprikol.ru/wp-content/uploads/2019/06/kartinki-krasivyh-devushek-skachat-besplatno-1.jpg"));
        userProfiles.put(5, new Profile(5, "VelvetHell", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSm69gI1jAbvnvVHtH_y7P1cWc7J7rgJzLbqYwPqi1MN5kxf4VP9dTV5WQ9r5SdXQ280Hc&usqp=CAU"));
        userProfiles.put(6, new Profile(6, "FluffySurprize", "https://oir.mobi/uploads/posts/2020-01/1579144434_1-1.jpg"));
        userProfiles.put(7, new Profile(7, "QUEEN", "https://moi-nikopol.dp.ua/wp-content/uploads/2021/11/smeshnye-fotki-devushek-stavshih-zhertvami-gore-stilistov-foto_61a4a440bdff3.jpeg"));
        userProfiles.put(8, new Profile(8, "LaDyCrUsH", "https://cdn.trinixy.ru/pics5/20170825/beautiful_girls_33.jpg"));
        userProfiles.put(9, new Profile(9, "CandyGirl", "https://i.pinimg.com/originals/00/25/34/002534402d42421a6530b05b80ec31f6.jpg"));
        userProfiles.put(10, new Profile(10,"Angel", "http://cn15.nevsedoma.com.ua/photo/11/1186/1610_files/beautiful_girls_02.jpg"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        config.setDirectoryForTemplateLoading(new File("src/main/resources/html"));

        Map<String, Object> data = new HashMap<>();
        data.put("username", userProfiles.get(userIdCounter).getNickName());
        data.put("image", userProfiles.get(userIdCounter).getPhotoSource());
        if (userIdCounter < userProfiles.values().size()) userIdCounter++;
        else userIdCounter = 1;
        userLog.add(req.getPathInfo());
        try (PrintWriter writer = resp.getWriter()) {
            config.getTemplate("like-page.html").process(data, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userLog.add(req.getPathInfo());
        this.doGet(req, resp);
    }
}
