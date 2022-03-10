package com.coldnight.book.utils;

import android.app.Activity;
import android.view.ViewTreeObserver;

import com.coldnight.book.beans.Chapter;
import com.coldnight.book.controls.ReadView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageUtils {

    private int chapter = 1;    //章节号，默认1
    private final List<Chapter> novel = new ArrayList<>();             //存放整本小说
    private final List<String> directory = new ArrayList<>();          //小说目录

    private final ReadView readView;
    private boolean oneIsLayout = false;

    public PageUtils(ReadView readView) {
        this.readView = readView;
    }

    // FIXME: 2021/10/12 此为默认分章方式，后期优化

    /**
     *  传入小说内容，自动分章
     *  分章的小说存到novel中
     * @param txt 传入需要分章的小说
     */
    public void getChapters(String txt) {
        getChapters(txt, "([第][\\d零一二三四五六七八九十百千万]{1,10}[章节回][ ]?[\\u4e00-\\u9fa5]{0,20}\n)");
    }

    public void getChapters(String txt, String regex) {
        getChapters(txt, regex, chapter);
    }

    public void getChapters(String txt, String regex, int chapter) {
        if ("".equals(txt.trim()))
            throw new RuntimeException("The text content must not be empty");
        List<Integer> positions = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(txt);
        while (m.find()) {
            directory.add(m.group().substring(0, (m.group().length() - 1)));
            positions.add(m.start());
            positions.add(m.end());
        }
        positions.add(txt.length());
        for (int i = 0; i < directory.size(); i++) {
            String endStr = txt.substring(positions.get(i * 2 + 1), positions.get(i * 2 + 2));
            novel.add(new Chapter(i + 1, directory.get(i), endStr));
        }

        //找不到标题分章，采用字数分章
        if (directory.size() == 0 || !txt.startsWith(directory.get(0))) {
            novel.clear();
            directory.clear();
            if (txt.length() < 5000) {
                directory.add("第一部分");
                novel.add(new Chapter(1, "第一部分", txt));
            } else {
                int num = (int) Math.ceil(txt.length() / 5000f);
                for (int i = 0; i <= num; i++) {
                    String title = "第" + (i + 1) + "部分";
                    directory.add(title);
                    if ((i + 1) * 5000 <= txt.length()) {
                        novel.add(new Chapter(i + 1, title, txt.substring(i * 5000, (i + 1) * 5000)));
                    } else {
                        novel.add(new Chapter(i + 1, title, txt.substring(i * 5000)));
                    }
                }
            }
        }
    }

    /**
     *  传入章节内容进行分页
     *  需要线程操作，故无返回值
     *  可传入接口回调，实现分页完成的操作
     * @param activity 线程更新UI需要activity
     * @param content 需要分页的章节内容
     * @param calculated 回调接口
     */
    public void getPages(Activity activity, String content, Calculated calculated) {
        readView.setText(content);
        List<String> mList = new ArrayList<>();
        new Thread(() -> {
            ViewTreeObserver observer = readView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(() -> {
                if (oneIsLayout)
                    return;
                int cursor = 0;
                int length = content.length();
                while (cursor < length) {
                    int charNum = readView.getCharNum();
                    int temp = cursor + charNum;
                    if (temp == 0)
                        return;
                    if (temp <= length){
                        mList.add(content.substring(cursor, temp));
                        cursor = temp;
                    } else {
                        mList.add(content.substring(cursor, length));
                        cursor += cursor;
                    }
                    if (cursor < length) {
                        int finalCursor = cursor;
                        activity.runOnUiThread(() -> readView.setText(content.substring(finalCursor)));
                    }
                }
                if (calculated != null)
                    calculated.calculated(mList);
                oneIsLayout = true;
            });
        }).start();
    }

    public interface Calculated {
        void calculated(List<String> mList);
    }
}
