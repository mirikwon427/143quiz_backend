package garlicbears._quiz.domain.game.dto;

import java.util.List;

public class ResponseGameStartDto {

    private int status;

    private Long topicId;

    private Long sessionId;

    private List<GameStartQuestionDto> game;

    public ResponseGameStartDto() {}

    public ResponseGameStartDto(ResponseGameStartBuilder builder) {
        this.status = builder.status;
        this.topicId = builder.topicId;
        this.sessionId = builder.sessionId;
        this.game = builder.game;
    }

    public int getStatus() {
        return status;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public List<GameStartQuestionDto> getGame() {
        return game;
    }

    public static class ResponseGameStartBuilder{

        private int status;

        private Long topicId;

        private Long sessionId;

        private List<GameStartQuestionDto> game;

        public ResponseGameStartBuilder status (int status) {
            this.status = status;
            return this;
        }

        public ResponseGameStartBuilder topicId (Long topicId) {
            this.topicId = topicId;
            return this;
        }

        public ResponseGameStartBuilder sessionId (Long sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public ResponseGameStartBuilder game (List<GameStartQuestionDto> game) {
            this.game = game;
            return this;
        }

        public ResponseGameStartDto build() { return new ResponseGameStartDto(this); }

    }
}
