package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    /**
     * Default constructor.
     */
    public EncodingFilter() {
        // フィルタのインスタンスが生成される際に実行する内容を書く
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // 「(フィルタの処理が不要になったため)フィルタを破棄する」と言うときの処理を定義するメソッド
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // フィルタとしての実行内容を定義する

        /*
         setCharacterEncoding()メソッドは
         リクエストボディに含まれるデータの文字コードを
         指定した値に書き換えるメソッドです
         （文字化け防止）
        */
        // requestの文字コードをUTF-8に指定して書き換え(文字化け防止)
        request.setCharacterEncoding("UTF-8");
        // responseの文字コードをUTF-8に指定して書き換え(文字化け防止)
        response.setCharacterEncoding("UTF-8");

        /*
         chin.doFilter(request, response);より
         前に書くとサーブレットが処理を実行する前に処理し
         後に書くとサーブレット処理実行後に処理する
        */
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // フィルタの処理が初めて実行されるときの処理を定義する
    }

}
