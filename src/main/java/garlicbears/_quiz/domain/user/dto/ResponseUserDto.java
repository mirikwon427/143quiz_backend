package garlicbears._quiz.domain.user.dto;

import garlicbears._quiz.domain.user.entity.User;

public class ResponseUserDto {
    private long userId;
    private String email;
    private String nickname;
    private int birthYear;
    private int age;
    private String gender;
    private String location;

    public ResponseUserDto(long userId, String email, String nickname, int birthYear, int age, String gender, String location) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.birthYear = birthYear;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public static ResponseUserDto fromUser(User user) {
        return new ResponseUserDto(
                user.getUserSeq(),
                user.getUserEmail(),
                user.getUserNickname(),
                user.getUserBirthYear(),
                user.getUserAge(),
                user.getUserGender().getKoreanName(),
                user.getUserLocation().getKoreanName());
    }
}
