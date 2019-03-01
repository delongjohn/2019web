import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class wx_servlet extends HttpServlet {
    @Override
    public void init(){}

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        String echoStr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        out.println(echoStr);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String xml = IOUtils.toString(request.getInputStream(), "utf-8");
        RequestMessage message = null;
        try {
            message = Tools.getRequest(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println(Tools.ReplyMessage(message));
    }
}
