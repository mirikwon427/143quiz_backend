package garlicbears._quiz.domain;

public enum Location {
    Seoul("서울"),
    Gyeonggi("경기"),
    Incheon("인천"),
    Chungcheong("충청"),
    Jeolla("전라"),
    Gyeongsang("경상"),
    Jeju("제주"),
    Overseas("해외");

    private final String korean;

    Location(String korean) {
        this.korean = korean;
    }

    public String korean() {
        return korean;
    }

}

