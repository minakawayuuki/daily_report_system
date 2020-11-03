package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
         // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
        // インターフェースServletContext()をServletContext型の変数contextにで使えるようにする
        ServletContext context = arg0.getServletContext();

        // getRealPath("使用するファイルを指定")は指定された仮装パスに対応する実際のパスを取得する
        // getRealPath()をString型の変数pathに入れる
        String path = context.getRealPath("/META-INF/application.properties");
        try {
            // インスタンス化したFileInputStream("使用するファイル")をInputStream型の変数isに入れる
            InputStream is = new FileInputStream(path);
            // インスタンス化したProperties()をProperties型のpropertiesに入れる
            Properties properties = new Properties();
            // load()はプロパティ・リストを入力文字ストリームから単純な行指向形式で読み込みます
            // isからキーと要素が対になったプロパティ・リストを読み込みます
            properties.load(is);
            // is(FileInputStream)を閉じます
            is.close();

            /*
            stringPropertyNames()はファイルの中身を取得する
            (今回の場合はapplication.propretiesの記述pepper=6Ab3mtmGのpepperを取得する)
             */
            // iterator()でイテレータクラスを使えるようにする(Setクラスのiterator()メソッドを使用)
            /*
            stringpropertyNames()でファイルの中身を取得しiterator()クラスを使えるようにしたのを
            Iterator<String>型の変数pitに入れる*/
            Iterator<String> pit = properties.stringPropertyNames().iterator();
            // hasNext()は次のデータがある場合にtrueを返す
            // ファイルのpepper=6Ab3mtmGなどの記述を読み出す繰り返し
            while(pit.hasNext()) {
                // next()は反復処理で次の要素を返します
                //pit.next(pepper)をString型の変数pname（取り出す際はpepper）に入れる
                String pname = pit.next();
                // String型変数pname（pepper）をServletContextにsetAttributeしている
                context.setAttribute(pname, properties.getProperty(pname));
            }
          // FileNotFoundExceptionは指定されたパス名で示されるファイルが開なかったことを通知します
        } catch(FileNotFoundException e) {
          // IOExceptionは入出力処理の失敗、または割り込みの発生によって生成される例外の汎用クラス
        } catch(IOException e) {}
    }

}