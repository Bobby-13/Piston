package com.example.CompilerApplication.service;

import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PistonService {

    ResponseEntity<?> CodeExecution(CodeExecutionRequest request,String id,String round_id,String contest_id);

    ResponseEntity<?> CodeSubmission(CodeExecutionRequest request, String id, String round_id, String contest_id);

    ResponseEntity<?> FetchLanguages();
}
