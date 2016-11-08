package cn.telbox.microarch.sample.embeddedjetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * This shows how to config the Jetty server from your jetty xml config files.
 *
 * Reference:
 *      https://wiki.eclipse.org/Jetty/Reference/jetty.xml_syntax
 *      https://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 *
 * Created by dahui on 05/11/2016.
 */
public class EmbeddedJettyServerWithConfigFile {
    public static void main(String[] args) throws Exception {
        Resource fileserver_xml = Resource.newSystemResource("/jetty.xml");
        XmlConfiguration configuration = new XmlConfiguration(fileserver_xml.getInputStream());
        Server server = (Server) configuration.configure();
        server.start();
        server.join();
    }
}
