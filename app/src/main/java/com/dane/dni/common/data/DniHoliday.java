package com.dane.dni.common.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dane on 12/7/2015.
 */
public class DniHoliday implements Parcelable {

    public static final Parcelable.Creator<DniHoliday> CREATOR
            = new Parcelable.Creator<DniHoliday>() {
            public DniHoliday createFromParcel(Parcel in) {
                return new DniHoliday(in);
            }

            public DniHoliday[] newArray(int size) {
                return new DniHoliday[size];
            }
    };

    private String name;
    private long vailee;
    private long yahr;

    public DniHoliday(String name, long vailee, long yahr) {
        this.name = name;
        this.vailee = vailee;
        this.yahr = yahr;
    }

    public DniHoliday(Parcel in) {
        long[] data = new long[2];
        in.readLongArray(data);
        name = in.readString();
        vailee = data[0];
        yahr = data[1];
    }

    public String getName() {
        return name;
    }

    public long getVailee() {
        return vailee;
    }

    public long getYahr() {
        return yahr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static byte[] marshall(DniHoliday parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static DniHoliday unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return new DniHoliday(parcel);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long[] data = new long[2];
        data[0] = vailee;
        data[1] = yahr;
        dest.writeLongArray(data);
        dest.writeString(name);
    }
}
