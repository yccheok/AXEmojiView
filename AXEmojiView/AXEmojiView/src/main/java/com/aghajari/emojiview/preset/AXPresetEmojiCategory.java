package com.aghajari.emojiview.preset;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiData;
import com.aghajari.emojiview.repository.EmojiDao;
import com.aghajari.emojiview.repository.EmojiRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class AXPresetEmojiCategory implements EmojiCategory {
    private final Context context;

    public Emoji[] emojiData;
    public String title;
    int icon;

    public AXPresetEmojiCategory(Context context, int i, int icon, EmojiData emojiData) {
        this.context = context;

        String[][] dataColored = emojiData.getDataColored();
        this.emojiData = new Emoji[dataColored[i].length];
        for (int j = 0; j < dataColored[i].length; j++)
            this.emojiData[j] = createEmoji(dataColored[i][j], i, j, emojiData);
        title = emojiData.getTitle(i);
        this.icon = icon;

        generateSQLite(i + 1);
    }

    private void generateSQLite(int category) {
        /*
        id
        unicode
        group
        category
        selected
         */

        List<com.aghajari.emojiview.model.Emoji> emojis = new ArrayList<>();

        for (Emoji emoji : this.emojiData) {
            com.aghajari.emojiview.model.Emoji emojiModel = new com.aghajari.emojiview.model.Emoji();
            emojiModel.unicode = emoji.getUnicode();
            emojiModel.category = category;

            List<Emoji> variants = emoji.getVariants();
            if (variants.isEmpty()) {
                emojiModel.group = null;
                emojiModel.selected = true;
                emojis.add(emojiModel);
            } else {
                emojiModel.group = emoji.getUnicode();
                emojiModel.selected = true;
                emojis.add(emojiModel);

                for (Emoji v : variants) {
                    emojiModel = new com.aghajari.emojiview.model.Emoji();
                    emojiModel.unicode = v.getUnicode();
                    emojiModel.category = category;
                    emojiModel.group = emoji.getUnicode();
                    emojiModel.selected = false;
                    emojis.add(emojiModel);
                }
            }
        }

        EmojiDao emojiDao = EmojiRoomDatabase.instance(context).emojiDao();
        emojiDao.insertAll(emojis);
    }


    protected Emoji createEmoji(String code, int category, int index, EmojiData emojiData){
        return new AXPresetEmoji(code, emojiData);
    }

    @NonNull
    @Override
    public Emoji[] getEmojis() {
        return emojiData;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }
}