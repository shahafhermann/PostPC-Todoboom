package com.ppc.todoboom;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String description;
    private boolean complete;
    private String id;
    private Date creationTime;
    private Date lastEditTime;

    /**
     * Empty constructor for FireStore usage
     */
    Task() {}

    /**
     * Construct a new Task with the given description
     * @param description Description of the task
     */
    Task(String description) {
        this.description = description;
        this.complete = false;
        this.creationTime = new Date(System.currentTimeMillis());
        this.lastEditTime = this.creationTime;
    }

    /**
     * Private Constructor for Parcel
     * @param in a Parcel to be reconstructed
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Task(Parcel in){
        this.description = in.readString();
        this.complete = in.readBoolean();
        this.id = in.readString();
        this.creationTime = new Date(in.readLong());
        this.lastEditTime = new Date(in.readLong());
    }

    /**
     * Check if the task was completed
     */
    public boolean isComplete() { return complete; }

    /**
     * Get the current tasks description
     */
    public String getDescription() { return description; }

    /**
     * Get the task id
     */
    public String getId() { return this.id; }

    /**
     * Get the task creation time
     */
    public Date getCreationTime() { return this.creationTime; }

    /**
     * Get the task last edit time
     */
    public Date getLastEditTime() { return this.lastEditTime; }

    /**
     * Set the task's descriptions to the given one
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Mark this task as complete
     */
    public void setComplete(boolean complete) { this.complete = complete; }

    /**
     * Set the task's id to the given one
     */
    public void setId(String id) { this.id = id; }

    /**
     * Update the task's last edit time
     */
    public void updateLastEditTime() {
        this.lastEditTime = new Date(System.currentTimeMillis());
    }

    //    //* Parcelable Interface Methods Below *//
//    /**
//     * Describe the kinds of special objects contained in this Parcelable instance's marshaled
//     * representation.
//     * @return a bitmask indicating the set of special object types marshaled by this Parcelable
//     *         object instance.
//     */
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    /**
//     * Flatten this object in to a Parcel.
//     * @param out The Parcel in which the object should be written.
//     * @param flags Additional flags about how the object should be written.
//     */
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(description);
//        out.writeBoolean(complete);
//    }
//
//    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){
//
//        /**
//         * Create a new instance of the Parcelable class, instantiating it from the given Parcel
//         * whose data had previously been written by Parcelable#writeToParcel.
//         * @param in The Parcel to read the object's data from.
//         * @return a new instance of the Parcelable class.
//         */
//        @RequiresApi(api = Build.VERSION_CODES.Q)
//        @Override
//        public Task createFromParcel(Parcel in) {
//            return new Task(in);
//        }
//
//        /**
//         * Create a new array of the Parcelable class.
//         * @param size Size of the array.
//         * @return an array of the Parcelable class, with every entry initialized to null.
//         */
//        @Override
//        public Task[] newArray(int size) {
//            return new Task[size];
//        }
//    };
}
