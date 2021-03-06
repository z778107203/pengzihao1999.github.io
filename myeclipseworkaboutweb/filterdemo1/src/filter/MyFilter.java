package filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class MyFilter
 */
@WebFilter("/*")
public class MyFilter implements Filter {
	/*
	 * 步骤：1.从application域中拿到map
	 *      2.获取用户访问时的ip地址
	 *      3.拿到ip地址在map中进行判断 如果已经存在使值+1 如果不存在设置值为1
	 *      
	 */
	private int MAX_NUM = 5;
	private FilterConfig fconfig; 
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("正在拦截");
		ServletContext application = fconfig.getServletContext();
        Map<String,Integer>	 map = (LinkedHashMap<String,Integer>)application.getAttribute("map");
        System.out.println(map);
        String Ip = request.getRemoteAddr();
        if(map.containsKey(Ip)){
        	int value = (Integer)map.get(Ip);
        	if(value>=MAX_NUM){
        		request.getRequestDispatcher("/error.jsp").forward(request, response);
        	}
        	map.put(Ip, ++value);
        }else{
        	map.put(Ip, 1);
        }
        System.out.println(map);
        application.setAttribute("map", map);
		chain.doFilter(request, response);//进行放行操作
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("我被初始化了");
		this.fconfig = fConfig;
	}


}
