package com.aghajari.emojiview.repository;

import androidx.room.Dao;
import androidx.room.Insert;

import com.aghajari.emojiview.model.Emoji;

import java.util.List;

@Dao
public interface EmojiDao {
    @Insert
    void insertAll(List<Emoji> emojis);
}
