package LibraryManagementService_Async.Handlers;

import LibraryManagementService_Async.Operations.Authentication;
import LibraryManagementService_Async.Operations.BookManagement;
import LibraryManagementService_Async.Operations.TransactionManagement;
import LibraryManagementService_Async.Utils.URIparser;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Class that handles all HTTP requests with POST method
 */
public class POSTHandler extends Handler{

    /**
     * An instance of Authentication Class that handles
     * tasks related to user login/logout
     *
     * @see Authentication
     */
    Authentication auth = new Authentication();

    /**
     * An instance of BookManagement Class that handles
     * tasks related to books
     *
     * @see BookManagement
     */
    BookManagement bookMgmt = new BookManagement();

    /**
     * An instance of TransactionManagement Class that handles
     * tasks related to book transactions
     *
     * @see TransactionManagement
     */
    TransactionManagement transMgmt = new TransactionManagement();

    /**
     * Method that handles all HTTP POST requests
     *
     * @param request HTTP request sent by the client (librarian)
     * @param response HTTP response that needs to be returned back to the client (librarian)
     * @param context represents execution state of an HTTP process
     * @throws MethodNotSupportedException if method implemented is not supported
     */
    @Override
    public void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) throws MethodNotSupportedException {
        // Handle Post method only
        String raw_path = request.getRequestLine().getUri();

        // Handle user login
        if(URIparser.parsedUri(raw_path).equals("/BookManagementService/login")){
            auth.handleLogin(request, response);
        }

        // Handle book add & lookup
        else if(URIparser.parsedUri(raw_path).equals("/BookManagementService/books")){
            if(request.getRequestLine().getMethod().equals("POST")){
                bookMgmt.addBooks(request, response);
            }
            if(request.getRequestLine().getMethod().equals("GET")){
                bookMgmt.lookBooks(request, response);
            }
        }

        else if(URIparser.parsedUri(raw_path).equals("/BookManagementService/transaction")){
            String entityContent = "";
            try{ entityContent = EntityUtils.toString(((HttpEntityEnclosingRequest) request).getEntity());}
            catch(IOException e){ System.out.println(e);}
            if(entityContent.equals("")) {
                transMgmt.requestTransactionId(request, response);
            }
            else{
                transMgmt.commitOrCancel(request, response, entityContent);
            }
        }

        else{
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
    }
}
