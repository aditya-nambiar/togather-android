package com.togather.me.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mouli on 7/27/15.
 */
@SuppressWarnings("unused")
public class GsonModels {

    public static class UserAccount implements Serializable {
        String gender;
        @SerializedName("first_name")
        String firstName;
        @SerializedName("last_name")
        String lastName;
        String email;
        @SerializedName("mobile_number")
        String phoneNumber;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }


    public static class ErrorInfo implements Serializable {
        String errorMessage;
        int duration;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }



}
