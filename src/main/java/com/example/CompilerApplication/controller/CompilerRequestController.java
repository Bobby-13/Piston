package com.example.CompilerApplication.controller;

import com.example.CompilerApplication.api.CompilerRequestApi;
import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.model.dto.CodeExecutionResponse;
import com.example.CompilerApplication.model.dto.LanguageInfoResponse;
import com.example.CompilerApplication.service.PistonService;
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
    public ResponseEntity<CodeExecutionResponse> ExecuteCode(CodeExecutionRequest request, String userId, String roundId, String contestId) {
        return pistonService.CodeExecution(request,userId,roundId,contestId);
    }

    @Override
    public ResponseEntity<CodeExecutionResponse> SubmitCode(CodeExecutionRequest request, String userId, String roundId, String contestId) {
        return pistonService.CodeSubmission(request,userId,roundId,contestId);
    }

    @Override
    public ResponseEntity<List<LanguageInfoResponse>> FetchLanguages() {
        return pistonService.FetchLanguages();
    }

    @Override
    public ResponseEntity<?> FetchCodingQuestion(String roundId) {
        return pistonService.FetchCodingQuestion(roundId);
    }

    @Override
    public ResponseEntity<?> FetchDraftCode(String userId, String roundId, long questionId) {
        return pistonService.FetchDraftCode(userId,roundId,questionId);
    }

}
