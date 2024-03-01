package com.example.compiler_application.controller;

import com.example.compiler_application.api.CompilerRequestApi;
import com.example.compiler_application.dto.*;
import com.example.compiler_application.service.PistonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CompilerRequestController implements CompilerRequestApi {

    private final PistonService pistonService;

    public CompilerRequestController(PistonService pistonService) {
        this.pistonService = pistonService;
    }

    @Override
    public ResponseEntity<ResponseDto> executeCode(CodeExecutionRequest request, String userId, String roundId, String contestId) {
        return pistonService.codeExecution(request,userId,roundId,contestId);
    }

    @Override
    public ResponseEntity<ResponseDto> submitCode(CodeExecutionRequest request, String userId, String roundId, String contestId) {
        return pistonService.codeSubmission(request,userId,roundId,contestId);
    }

    @Override
    public ResponseEntity<ResponseDto> fetchLanguages() {
        return pistonService.fetchLanguages();
    }

    @Override
    public ResponseEntity<ResponseDto> fetchCodingQuestion(String roundId) {
        return pistonService.fetchCodingQuestion(roundId);
    }

    @Override
    public ResponseEntity<ResponseDto> fetchDraftCode(String userId, String roundId, long questionId) {
        return pistonService.fetchDraftCode(userId,roundId,questionId);
    }

    @Override
    public ResponseEntity<ResponseDto> updateResult(String roundId, int passMark) {
        return pistonService.updateResult(roundId,passMark);
    }


}
