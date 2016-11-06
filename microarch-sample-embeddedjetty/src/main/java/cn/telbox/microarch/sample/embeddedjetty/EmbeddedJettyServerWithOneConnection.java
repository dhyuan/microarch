package cn.telbox.microarch.sample.embeddedjetty;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.component.Container;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

/**
 * Just try to config out the Jetty server with the statistic interface.
 * More: file:///Users/dahui/devEnv/servers/jetty/9.3.13.v20161014/embedding-jetty.html
 * <p>
 * Created by dahui on 05/11/2016.
 */
public class EmbeddedJettyServerWithOneConnection {

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);
        http_config.setRequestHeaderSize(32768); // request header size

        // Enable the X-Forwarded header customization
        ForwardedRequestCustomizer forwarded = new ForwardedRequestCustomizer();
        http_config.addCustomizer(forwarded);


        int acceptorCount = 8; // acceptor thread counts
        int selectorCount = -1; // allow connector defaults
        ServerConnector http = new ServerConnector(server, null, null, null, acceptorCount, selectorCount, new HttpConnectionFactory(http_config));
        http.setPort(8080);
        http.setIdleTimeout(30000); // idle timeout
        http.setAcceptQueueSize(8); // also known as backlog

        // Set the connectors
        server.setConnectors(new Connector[]{http});

        // Enable Statistics Gathering for connectors
        ConnectorStatistics.addToAllConnectors(server);

        // Enable Low Resources Management
        LowResourceMonitor lowResources = new LowResourceMonitor(server);
        lowResources.setLowResourcesIdleTimeout(10000);
        lowResources.setMaxConnections(10000);
        server.addBean(lowResources);

//
//        // Setup JMX
//        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
//        server.addEventListener(mbContainer);
//        server.addBean(mbContainer);


        // Set a handler
        StatisticsHandler statsHandler = new StatisticsHandler();
        statsHandler.setHandler(new HelloHandler("Hi There"));
        server.setHandler(statsHandler);

        server.setHandler(new HelloHandler("Hi There"));

        // Start the server
        server.start();
        server.join();

        // Dump connector stats after server is done running
        for (Connector connector : server.getConnectors()) {
            if (connector instanceof Container) {
                Container container = (Container) connector;
                ConnectorStatistics stats = container.getBean(ConnectorStatistics.class);
                System.out.printf("Connector: %s%n", connector);
                stats.dump(System.out, "  ");
            }
        }
    }

    public static class HelloHandler extends AbstractHandler {
        final String greeting;
        final String body;

        public HelloHandler() {
            this("Hello World");
        }

        public HelloHandler(String greeting) {
            this(greeting, null);
        }

        public HelloHandler(String greeting, String body) {
            this.greeting = greeting;
            this.body = body;
        }

        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
                ServletException {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();

            out.println("<h1>" + greeting + "</h1>");
            if (body != null) {
                out.println(body);
            }

            baseRequest.setHandled(true);
        }
    }

}
