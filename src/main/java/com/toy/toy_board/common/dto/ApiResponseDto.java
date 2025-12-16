package com.toy.toy_board.common.dto;


import lombok.Getter;

@Getter
public class ApiResponseDto<T> {
    private String code;
    private String message;
    private T data;

    //    Required생성자
    private ApiResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
//    all 생성자
    private ApiResponseDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

//    생성자 없이도 호출가능한 Static 메서드
    public static <T> ApiResponseDto<T> createOk(T data) {
        return new ApiResponseDto<>("OK", "데이터 생성 요청이 성공하였습니다.", data);
    }
    public static <T> ApiResponseDto<T> readOk(T data) {
        return new ApiResponseDto<>("OK", "데이터 조회 요청이 성공하였습니다.", data);
    }
    public static <T> ApiResponseDto<T> deleteOk(T data){
        return new ApiResponseDto<>("OK", "데이터 삭제를 완료하였습니다.", data);
    }

    public static ApiResponseDto<String> defaultOk() {
        return ApiResponseDto.createOk(null);
    }

    public static ApiResponseDto<String> createError(String code, String message) {
        return new ApiResponseDto<>(code, message);
    }

    public static <T> ApiResponseDto<T> createError(String code, String message, T data) {
        return new ApiResponseDto<>(code, message, data);
    }
}
