package cn.cestco.titlebarlibrary.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：谢青仂 on 2016/8/15
 * 邮箱：qingle6616@sina.com
 * <p/>
 * 判断字符串是否为空
 */
public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(CharSequence value) {
        if (value != null && !"".equalsIgnoreCase(value.toString().trim())
                && !"null".equalsIgnoreCase(value.toString().trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串组是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(CharSequence... values) {
        boolean isEmpty = true;
        for (CharSequence value : values) {
            if (isNotEmpty(value)) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;

    }

    /**
     * 判断字符串是否有值，如果不为null或者不是空字符串或者只没有有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isNotEmpty(CharSequence value) {
        if (value != null && !"".equalsIgnoreCase(value.toString().trim())
                && !"null".equalsIgnoreCase(value.toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串组是否有值，如果不为null或者不是空字符串或者只没有有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isNotEmpty(CharSequence... values) {
        boolean isNotEmpty = true;
        for (CharSequence value : values) {
            if (isEmpty(value)) {
                isNotEmpty = false;
                break;
            }
        }
        return isNotEmpty;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param a  匹配的字符串
     * @param bs 与之匹配的字符串组
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence... bs) {
        boolean equal = true;
        for (CharSequence b : bs) {
            if (!equals(a, b)) {
                equal = false;
                break;
            }
        }
        return equal;
    }

    /**
     * 弹出吐司
     *
     * @param context      上下文
     * @param charSequence 需弹出的字符串
     */
    public static void toast(Context context, String charSequence) {
        if (!isEmpty(charSequence)) {
            Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show();
        }
    }

    public static void getHtmlText(final String content, final Handler handler) {
        if (StringUtils.isEmpty(content))
            return;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Spanned html = Html.fromHtml(content, new Html.ImageGetter() {

                    @Override
                    public Drawable getDrawable(String source) {
                        String imageName = source.substring(source.lastIndexOf("/") + 1);
                        String cacheDir = ResourceUtils.getContext().getCacheDir().getAbsolutePath() + "/contentDetail/";
                        Drawable drawable = Drawable.createFromPath(cacheDir + imageName);
                        if (drawable != null) {
                            drawable.setBounds(0, 0,
                                    (int) (drawable.getIntrinsicWidth()
                                            * DensityUtils.getDipScale(ResourceUtils.getContext())),
                                    (int) (drawable.getIntrinsicHeight()
                                            * DensityUtils.getDipScale(ResourceUtils.getContext())));

                            return drawable;
                        }

                        try {
                            InputStream is = (InputStream) new URL(source).getContent();
                            drawable = Drawable.createFromStream(is, "src");
                            drawable.setBounds(0, 0,
                                    (int) (drawable.getIntrinsicWidth()
                                            * DensityUtils.getDipScale(ResourceUtils.getContext())),
                                    (int) (drawable.getIntrinsicHeight()
                                            * DensityUtils.getDipScale(ResourceUtils.getContext())));
                            File cacheFile = new File(cacheDir);
                            if (!cacheFile.exists()) {
                                cacheFile.mkdir();
                            }
                            BitmapDrawable bd = (BitmapDrawable) drawable;
                            Bitmap bitmap = bd.getBitmap();
                            FileOutputStream fos = new FileOutputStream(new File(cacheFile, imageName));
                            if (bitmap != null) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return drawable;
                    }

                }, null);
                handler.sendMessage(handler.obtainMessage(0, html));
                return null;
            }

        }.execute();
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 复制文本
     */
    public static void copyText(Context context, String copyText) {
        ClipboardManager copy = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(StringUtils.isEmpty(copyText) ? "E维社区" : copyText);
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
}
