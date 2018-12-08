package com.zhiyong.xiayibu;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizItem implements Parcelable {
    private long timestamp;
    private int response;

    public QuizItem(long timestamp, int response) {
        this.timestamp = timestamp;
        this.response = response;
    }

    protected QuizItem(Parcel in) {
        timestamp = in.readLong();
        response = in.readInt();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizItem quizItem = (QuizItem) o;

        if (timestamp != quizItem.timestamp) return false;
        return response == quizItem.response;
    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + response;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeInt(response);
    }

    public static final Creator<QuizItem> CREATOR = new Creator<QuizItem>() {
        @Override
        public QuizItem createFromParcel(Parcel source) {
            return new QuizItem(source);
        }

        @Override
        public QuizItem[] newArray(int size) {
            return new QuizItem[size];
        }
    };
}
