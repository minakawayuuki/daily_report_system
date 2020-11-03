package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {
    // daily_report_systemを入れたString型の静的フィールド(定数)PERSISTENCE_UNIT_NAMEを作成
    private static final String PERSISTENCE_UNIT_NAME = "daily_report_system";
    // EntityManagerFactory型の静的フィールド(定数)emfを定義
    private static EntityManagerFactory emf;

    //createEntityManager()は新しいアプリケーション管理のEntityManagerを作成する
    //EntityManager型のcreateEntityManager()静的メソッドを作成します
    public static EntityManager createEntityManager() { // このメソッドはEntityManagerを作成するエンティティマネージャーファクトリーを返す
     // _getEntityManagerFactory().createEntityManager()を呼び出し元に返す
        return _getEntityManagerFactory().createEntityManager();
    }
    // emfがnullの時PERSISTENCE_UNIT_NAME（daily_report_systemの）EntityManagerを作成し返すメソッド
    // EntityManagerFactory型の_getEntityManagerFactory()静的メソッドを作成
    private static EntityManagerFactory _getEntityManagerFactory() {
        // emfがnullの時実行する
        if(emf == null) {
            /*
             createEntityManagerFactory()は
             指定された永続性のユニットのEntityManagerを作成して返します
             */
            //PERSISTENCE_UNIT_NAMEの永続性ユニットEntityManagerを作成してemfに入れ作成
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }

        // emfを呼び出し元に返す
        return emf;
    }
}
