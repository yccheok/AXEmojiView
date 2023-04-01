package com.aghajari.emojiview.preset;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiData;

import java.util.List;

public class AXPresetEmojiCategory implements EmojiCategory {
    public Emoji[] emojiData;
    public String title;
    int icon;

    public AXPresetEmojiCategory(int i, int icon, EmojiData emojiData) {
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
        if (category == 1) {
            for (Emoji emoji : this.emojiData) {
                List<Emoji> variants = emoji.getVariants();
                if (variants.isEmpty()) {
                    Log.i("CHEOK", emoji.getUnicode() + "," + "," + (category) + "," + true);
                } else {
                    Log.i("CHEOK", emoji.getUnicode() + "," + emoji.getUnicode() + "," + (category) + "," + true);
                    for (Emoji v : variants) {
                        Log.i("CHEOK", v.getUnicode() + "," + emoji.getUnicode() + "," + (category) + "," + false);
                    }
                }
            }
        }    
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