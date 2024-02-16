package com.example.CompilerApplication.api;

import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.model.dto.CodeExecutionResponse;
import com.example.CompilerApplication.model.dto.LanguageInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public interface CompilerRequestApi {

    @PostMapping("/compile/{userId}/{roundId}/{contestId}")
    ResponseEntity<CodeExecutionResponse> ExecuteCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @PostMapping("/submit/{userId}/{roundId}/{contestId}")
    ResponseEntity<CodeExecutionResponse> SubmitCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @GetMapping("/languages")
    ResponseEntity<List<LanguageInfoResponse>> FetchLanguages();

   @GetMapping("/codingQuestion/{roundId}")
    ResponseEntity<?> FetchCodingQuestion(@PathVariable String roundId);

   @GetMapping("/draftCode/{userId}/{roundId}/{questionId}")
    ResponseEntity<?> FetchDraftCode(@PathVariable String userId, @PathVariable String roundId, @PathVariable long questionId);
}
