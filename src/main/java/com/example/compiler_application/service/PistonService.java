package com.example.compiler_application.service;

import com.example.compiler_application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PistonService {

    ResponseEntity<ResponseDto> codeExecution(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<ResponseDto> codeSubmission(CodeExecutionRequest request, String userId, String roundId, String contestId);

    ResponseEntity<ResponseDto> fetchLanguages();

    ResponseEntity<ResponseDto> fetchCodingQuestion(String roundId);

    ResponseEntity<ResponseDto> fetchDraftCode(String userId, String roundId, long questionId);

    ResponseEntity<ResponseDto> updateResult(String roundId, int passMark);
}
