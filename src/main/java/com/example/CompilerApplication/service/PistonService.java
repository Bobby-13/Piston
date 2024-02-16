package com.example.CompilerApplication.service;

import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.model.dto.CodeExecutionResponse;
import com.example.CompilerApplication.model.dto.LanguageInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PistonService {

    ResponseEntity<CodeExecutionResponse> CodeExecution(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<CodeExecutionResponse> CodeSubmission(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<List<LanguageInfoResponse>> FetchLanguages();

    ResponseEntity<?> FetchCodingQuestion(String roundId);

    ResponseEntity<?> FetchDraftCode(String userId, String roundId, long questionId);
}
