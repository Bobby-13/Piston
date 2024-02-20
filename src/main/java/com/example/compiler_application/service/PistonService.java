package com.example.compiler_application.service;

import com.example.compiler_application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PistonService {

    ResponseEntity<CodeExecutionResponse> codeExecution(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<CodeExecutionResponse> codeSubmission(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<List<LanguageInfoResponse>> fetchLanguages();

    ResponseEntity<List<QuestionDto>> fetchCodingQuestion(String roundId);

    ResponseEntity<List<DraftCodeResponseDto>> fetchDraftCode(String userId, String roundId, long questionId);
}
