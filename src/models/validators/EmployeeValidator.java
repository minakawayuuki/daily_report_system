package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

public class EmployeeValidator {
    // 社員コードやパスワードなど記入もれがないかを確かめるvalidate()メソッド
    // List<String>型の引数ありコンストラクタの定数validate()メソッドを作成
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag) {
        // ArrayList<String>型の変数errorsを作成
        List<String> errors = new ArrayList<String>();

        // 引数ありのコンストラクタの定数_validateCode()をString型の変数code_errorに入れる
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        // code_errorが空文字でない時実行
        if(!code_error.equals("")) {
            // errorsリストににcode_errorを追加する
            errors.add(code_error);
        }

        String name_error = _validateName(e.getName());
        if(!name_error.equals("")) {
            errors.add(name_error);
        }

        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")) {
            errors.add(password_error);
        }

        // errorsを呼び出し下に返す
        return errors;

    }

    // 社員番号作成時にすでに存在しているかどうかを確かめるvalidateCode()メソッド
    // String型の引数ありコンストラクタの定数validateCode()メソッドを作成
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        // codeがnullの時またはcodeが空白の時実行
        if(code == null || code.equals("")) {
            // "社員番号を 後省略"を呼び出し元に返す
            return "社員番号を入力してください。";
        }

        // すでに登録されている社員番号との重複チェック
        /*
         code_duplicate_check_flagがtrueの時実行
         Boolean型はtrueかfalseを表す(この場合何か入っていればtrue)
         */
        if(code_duplicate_check_flag) {
            /*
             DBUtil.javaのcreateEntityManager()メソッドをEntityManager型の変数emにいれる
             (EntityManagerクラスを生成しemに入れる)
             */
            EntityManager em = DBUtil.createEntityManager();
            /*
             createNamedQuery()メソッドは名前付き(JPQLまたはネイティブSQLの)クエリーを実行するための
             Queryインスタンスを作成します
             createNamedQuery("実行するquery文につけた名前", 実行時に取得する型)
             */
            /*
            long型のemployees_countにcheckRefisterdCode(指定された社員番号がすでに
            データベースに存在しているか調べる)の実行結果をlong型にキャストしていれます
             */
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                    // setParameter("変数名", 代入する値)です
                    .setParameter("code", code)
                    // 一つの型のない結果を返すSELECTクエリーを実行します
                    .getSingleResult();

            // em(EntityManager)を閉じる
            em.close();
            // employees_count(社員番号が)0より多くある時に実行
            if(employees_count > 0) {
                // "入力された 後省略"を呼び出し下に返す
                return "入力された社員番号の情報はすでに存在しています。";
            }
        }
        // ""(空白文字を)呼び出し元に返す
        return "";
    }

    // 社員名の必須入力チェック
    private static String _validateName(String name) {
        if(name == null || name.equals("")) {
            return "氏名を入力してください";
        }

        return "";

    }

    // パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {
        // パスワードを変更する場合のみ実行
        if(password_check_flag && (password == null || password.equals(""))) {
            return "パスワードを入力してください。";
        }
        return "";
    }
}
