/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package LibraryManagementService_Async;

import LibraryManagementService_Async.Handlers.Handler;
import org.apache.http.ExceptionLogger;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The LibraryManagementService program implements a RESTFul Web Application that
 * handles back-end operations for authentication, book management and transactions
 *
 * @author Hyunho Kim
 * @since 2020-05-25
 */
public class App {

    /**
     * Set of routes that needs to be handled
     */
    private static final Routes ROUTES = new Routes();

    /**
     * Method that is executed upon start of the Web Application
     *
     * @param args indicates additional parameters that can be inserted
     */
    public static void main(String[] args) {

        final IOReactorConfig config = IOReactorConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        final var serverBuilder = ServerBootstrap.bootstrap()
                .setListenerPort(8080)
                .setIOReactorConfig(config)
                .setExceptionLogger(ExceptionLogger.STD_ERR);

        registerHandlers(serverBuilder);

        final HttpServer server = serverBuilder.create();

        try{
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdown(2, TimeUnit.SECONDS)));
            server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }catch(IOException | InterruptedException e){
            System.out.println(e);
        }
    }

    /**
     * Method that retrieves all handlers to be used for handling HTTP requests
     *
     * @param serverBuilder indicates Sever setup that allows registration of given handlers
     */
    public static void registerHandlers(ServerBootstrap serverBuilder){
        Map<String, Handler> getHandlers = ROUTES.getPatternForGet();
        Map<String, Handler> postHandlers = ROUTES.getPatternForPost();
        Map<String, Handler> putHandlers = ROUTES.getPatternForPut();
        Map<String, Handler> deleteHandlers = ROUTES.getPatternForDelete();

        getHandlers.forEach(serverBuilder::registerHandler);
        postHandlers.forEach(serverBuilder::registerHandler);
        putHandlers.forEach(serverBuilder::registerHandler);
        deleteHandlers.forEach(serverBuilder::registerHandler);
    }
}
