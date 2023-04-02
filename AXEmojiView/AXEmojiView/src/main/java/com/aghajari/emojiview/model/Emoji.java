package com.aghajari.emojiview.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "emoji",
    indices = {
        @Index(value = "unicode", unique = true),
        @Index("group"),
        @Index("category")
    }
)
public class Emoji {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "unicode")
    @NonNull
    public String unicode;

    @ColumnInfo(name = "group")
    public String group;

    @ColumnInfo(name = "category")
    public int category;

    @ColumnInfo(name = "selected")
    public boolean selected;
}
