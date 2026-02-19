package com.board.one_more_project.global.error.exception;

// AI 서버 통신 장애 시 발생시킬 전용 예외
public class AiServerException extends RuntimeException {
    public AiServerException(String message) {
        super(message);
    }
}