package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class EncryptUtil {
    // パスワードをハッシュ化するためのメソッド
    // String型のgetPasswordEncrypt()引数ありの静的メソッドを作成
    public static String getPasswordEncrypt(String plain_p, String pepper) {
        // ""をString型の変数retに入れる
        String ret = "";

        // plain_pがnullでない時かつplain_pが空文字でない時実行
        if(plain_p != null && !plain_p.equals("")) {
            // byte[](配列)型のbytesを定義
            byte[] bytes;
            // plain_p + pepperをString型の変数passwordに入れる
            String password = plain_p + pepper;
            try {
                // getInstance()は指定されたダイジェスト・アルゴリズムを実装するMessageDigestオブジェクトを返します
                // digest()はパディングなどの最終処理を行ってハッシュ計算を完了します
                /*
                getBytes()は指定された文字セットを使用してこのStringをバイト・シーケンスにエンコード化し、
                結果を新規バイト配列に格納します
                 */
                /*
                passwordをバイト・シーケンスに形式に変換（エンコード）しSHA-256にハッシュ化したものを
                byte[](配列)型のbytesに入れる
                 */
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
                // printHexBinary()はバイト配列を文字列に変換します
                // 文字列に変換したbytesをString型変数のretに入れる
                ret = DatatypeConverter.printHexBinary(bytes);
              // ある暗号アルゴリズムが要求されたにもかかわらず、現在の環境では使用可能でない場合にスローされます
            } catch(NoSuchAlgorithmException ex) {}
        }
        // retを呼び出し元に返す
        return ret;
    }
}