package com.ppc.todoboom;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Task implements Parcelable{
    private String description;
    private boolean done;

    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    public void complete() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public String getDescription() {
        return description;
    }


    //* Parcelable Interface Methods Below *//
    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
     * representation.
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
     *         object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param out The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(description);
        out.writeBoolean(done);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel
         * whose data had previously been written by Parcelable#writeToParcel.
         * @param in The Parcel to read the object's data from.
         * @return a new instance of the Parcelable class.
         */
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param size Size of the array.
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    /**
     * Private Constructor
     * @param in a Parcel to be reconstructed
     */
    private Task(Parcel in){
        description = in.readString();
        done = in.readBoolean();
    }
}
