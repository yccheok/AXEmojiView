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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AXPresetEmojiCategory implements EmojiCategory {
    private final Context context;

    public Emoji[] emojiData;
    public String title;
    int icon;

    // https://unicode.org/Public/emoji/15.0/emoji-test.txt
    // https://apps.timwhitlock.info/unicode/inspect
    Map<String, String> toFullyQualified = new HashMap<String, String>() {{
        put("☺", "☺️");
        put("☹", "☹️");
        put("✌", "✌️");
        put("☝", "☝️");
    }};

    Set<String> ignoredEmojis = new HashSet<>(
        Arrays.asList(
            "\uD83E\uDDB0",
            "\uD83E\uDDB1",
            "\uD83E\uDDB3",
            "\uD83E\uDDB2"
        )
    );

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
            if (ignoredEmojis.contains(emoji.getUnicode())) {
                continue;
            }

            final String fullyQualifiedUnicode;
            if (toFullyQualified.containsKey(emoji.getUnicode())) {
                fullyQualifiedUnicode = toFullyQualified.get(emoji.getUnicode());
            } else {
                fullyQualifiedUnicode = emoji.getUnicode();
            }

            com.aghajari.emojiview.model.Emoji emojiModel = new com.aghajari.emojiview.model.Emoji();
            emojiModel.unicode = fullyQualifiedUnicode;
            emojiModel.category = category;

            List<Emoji> variants = emoji.getVariants();
            if (variants.isEmpty()) {
                emojiModel.group = null;
                emojiModel.selected = true;
                emojis.add(emojiModel);
            } else {
                emojiModel.group = fullyQualifiedUnicode;
                emojiModel.selected = true;
                emojis.add(emojiModel);

                for (Emoji v : variants) {
                    emojiModel = new com.aghajari.emojiview.model.Emoji();
                    emojiModel.unicode = v.getUnicode();
                    emojiModel.category = category;
                    emojiModel.group = fullyQualifiedUnicode;
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