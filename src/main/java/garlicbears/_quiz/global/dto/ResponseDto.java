package garlicbears._quiz.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private int status;
    private String code;

    public static <T> ResponseDto<T> success(){
        return new ResponseDto<>(200, "SUCCESS");
    }
}
