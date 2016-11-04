package com.microsoft.xuetang.component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.microsoft.xuetang.util.SimplePair;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by shijianguang on 10/11/16.
 */
@Component
public class UserProfileComponent {
    private static List<List<SimplePair<String, Float>>> fakeProfile;
    private static Map<String, List<SimplePair<String, Float>>> fakeProfileMap;
    private static List<String> queryBall;
    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    static {
        fakeProfile = new ArrayList<>();
        queryBall = new ArrayList<>();
        fakeProfileMap = new HashMap<>();

        List<String> line = new ArrayList<String>() {
            {
                add("Deep Learning");
                add("神经网络");
                add("深度学习");
            }
        };
        queryBall.addAll(line);
        List<SimplePair<String, Float>> profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("人工智能");
                add("自然语言处理");
                add("Reinforcement Learning");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("云计算");
                add("Infrastructure as a Service");
                add("Platform as a Service");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("WEB开发");
                add("Java Web");
                add("WEB开发框架");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("大数据");
                add("Hadoop");
                add("Spark Streaming");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("机器学习");
                add("Machine Learning");
                add("Scikit Learn");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);

        line = new ArrayList<String>() {
            {
                add("信息检索");
                add("搜索引擎");
            }
        };
        queryBall.addAll(line);
        profile = new ArrayList<>();
        for(String ele : line) {
            profile.add(new SimplePair<>(ele, 1f));
        }
        fakeProfile.add(profile);
        fakeProfileMap.put(line.get(0), profile);
    }

    public static List<SimplePair<String, Float>> getUserProfile() {
        int idx = random.nextInt(fakeProfile.size());
        return fakeProfile.get(idx);
    }

    public static List<SimplePair<String, Float>> getUserProfile(String query) {
        return fakeProfileMap.get(query);
    }

    public static List<String> getQueryBall() {
        return queryBall;
    }
}
