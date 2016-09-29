package com.microsoft.xuetang;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jiash on 8/1/2016.
 */
public class Main implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_THREAD_SIZE = 20;
    private static final String CONTEXT_PATH = "/";
    private static final String WAR = "webapp";
    private static final int GRACEFUL_SHUTDOWN_TIMEOUT = 3000;
    private int port;
    private Server server;


    public Main(int port) throws Exception {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        WebAppContext webApp = new WebAppContext();
        //webApp.setDescriptor(Main.class.getResource("/WEB-INF/web.xml").toString());
        webApp.setContextPath(CONTEXT_PATH);
        webApp.setWar(WAR);
        webApp.getServletContext().getContextHandler().setMaxFormKeys(1000000000);
        webApp.getServletContext().getContextHandler().setMaxFormContentSize(5 * 1024 * 1024);


        //Configuration configuration = context.getBean(Configuration.class);
        //BillingService billingService = context.getBean(BillingService.class);

        //        ContextHandler localProcessHandlerZhenshi = new ContextHandler("/htc/app/v1/b/local_process_zhenshi2");
        //        localProcessHandlerZhenshi.setHandler(new LocalCameraProcessHandlerZhenshi(configuration, billingService));
        //        localProcessHandlerZhenshi.setAllowNullPathInfo(true);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{
                //                localProcessHandlerZhenshi,
        });

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{new StatisticsHandler(), webApp, contexts});

        this.server = new Server(new QueuedThreadPool(DEFAULT_THREAD_SIZE));
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 5 * 1024 * 1024);
        server.setAttribute("jetty.request.header.size", 5 * 1024 * 1024);
        server.setAttribute("jetty.output.buffer.size", 5 * 1024 * 1024);
        server.setHandler(handlers);

        server.setStopAtShutdown(true);
        server.setStopTimeout(GRACEFUL_SHUTDOWN_TIMEOUT);
    }

    public static void main(String[] args) throws Exception {
        int port = 4088;

        if (args.length >= 1) {
            logger.info("args {}", args[0]);
            port = Integer.valueOf(args[0]);
        }
        logger.info("user port {}", port);

        Main server = new Main(port);
        server.start();
        server.join();
    }

    public void start() throws Exception {
        try {
            this.server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void join() throws Exception {
        this.server.join();
    }

    @Override
    public void close() throws Exception {
        server.stop();
    }
}
