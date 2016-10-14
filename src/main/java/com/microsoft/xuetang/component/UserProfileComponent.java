package com.microsoft.xuetang.component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.microsoft.xuetang.util.SimplePair;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by shijianguang on 10/11/16.
 */
@Component
public class UserProfileComponent {
    private static List<List<SimplePair<String, Float>>> fakeProfile;
    private static List<String> queryBall;
    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    static {
        fakeProfile = new ArrayList<>();
        queryBall = new ArrayList<>();

        List<String> line = new ArrayList<String>() {
            {
                add("Deep Learning");
                add("神经网络");
                add("深度学习");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("人工智能");
                add("自然语言处理");
                add("Reinforcement Learning");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("云计算");
                add("Infrastructure as a Service");
                add("Platform as a Service");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("WEB开发");
                add("Java Web");
                add("WEB开发框架");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("大数据");
                add("Hadoop");
                add("Spark Streaming");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("机器学习");
                add("Machine Learning");
                add("Scikit Learn");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));

        line = new ArrayList<String>() {
            {
                add("信息检索");
                add("搜索引擎");
            }
        };
        queryBall.addAll(line);
        fakeProfile.add(Lists.transform(line, new Function<String, SimplePair<String, Float>>() {
            @Override public SimplePair<String, Float> apply(String input) {
                return new SimplePair<>(input.toLowerCase(), 1f);
            }
        }));
    }

    public static List<SimplePair<String, Float>> getUserProfile() {
        int idx = random.nextInt(fakeProfile.size());
        return fakeProfile.get(idx);
    }

    public static List<String> getQueryBall() {
        return queryBall;
    }
}
