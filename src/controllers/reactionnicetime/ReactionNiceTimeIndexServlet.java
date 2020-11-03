package controllers.reactionnicetime;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.ReactionNiceTime;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReactionNiceTimeIndexServlet
 */
@WebServlet("/reactionnicetime/index")
public class ReactionNiceTimeIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReactionNiceTimeIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()メソッドを実行しEntityManagerを作成し
        EntityManager型の変数emに入れる*/
        EntityManager em = DBUtil.createEntityManager();

        // フォームからString型の"id"を入手しInteger型にキャストしたのをInteger型の変数idに入れる
        Integer id = Integer.parseInt(request.getParameter("id"));

        // Integer型の変数idをem(データベース)のReportクラスから探したのをReport型の変数rntに入れる
        Report rnt = em.find(Report.class, id);

        // int型の変数pageを定義
        int page;
        try{
            // フォームから入手した"page"をInteger型にキャストしてint型の変数pageに入れ作成
            page = Integer.parseInt(request.getParameter("page"));
          // 例外処理(ErrorやRuntimeException以外の例外、別名検査例外とも呼ばれる)
        } catch(Exception e) {
            // int型の変数pageに１を入れて作成
            page = 1;
        }
        /*
        ReactionNiceTimeクラスのNamedQueryのgetMyAllReactionNiceTimeをおこなったのを
        List<ReactionNiceTime>型の変数reactionnicetimesに入れ作成*/
        List<ReactionNiceTime> reactionnicetimes = em.createNamedQuery("getMyAllReactionNiceTime", ReactionNiceTime.class)
                // queryの:reportにrntを入れる
                .setParameter("report", rnt)
                // 検索結果を取得する最初のポジションを指定する
                .setFirstResult(15 * (page - 1))
                // 取得する結果の最大件数を設定
                .setMaxResults(15)
                // 一件以上取得する際に使用
                .getResultList();
        /*
        ReactionNiceTimeクラスのNamedQueryのgetMyReactionNicetimeCountをおこなったのを
        long型の変数reactinnicetime_countに入れ作成*/
        long reactionnicetime_count = (long)em.createNamedQuery("getMyReactionNiceTimeCount", Long.class)
                // queryの:reportにrntを入れる
                .setParameter("report", rnt)
                // SELECTクエリーを実行し問合せ結果を型のないリストとして返す
                .getSingleResult();
        // em(データベース)を閉じる
        em.close();

        request.setAttribute("id", id);
        request.setAttribute("reactionnicetimes", reactionnicetimes);
        request.setAttribute("reactionnicetime_count", reactionnicetime_count);
        request.setAttribute("page", page);

        // flushがnullでない時にflushを消す
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        // /reactionnicetime/indexをビューに指定
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reactionnicetime/index.jsp");
        // レスポンス画面としてjspファイルを呼び出します
        rd.forward(request, response);
    }

}