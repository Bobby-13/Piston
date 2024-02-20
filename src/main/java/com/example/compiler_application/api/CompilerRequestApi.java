package com.example.compiler_application.api;

import com.example.compiler_application.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public interface CompilerRequestApi {

    @PostMapping("/compile/{userId}/{roundId}/{contestId}")
    ResponseEntity<CodeExecutionResponse> executeCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @PostMapping("/submit/{userId}/{roundId}/{contestId}")
    ResponseEntity<CodeExecutionResponse> submitCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @GetMapping("/languages")
    ResponseEntity<List<LanguageInfoResponse>> fetchLanguages();

   @GetMapping("/codingQuestion/{roundId}")
    ResponseEntity<List<QuestionDto>> fetchCodingQuestion(@PathVariable String roundId);

   @GetMapping("/draftCode/{userId}/{roundId}/{questionId}")
    ResponseEntity<List<DraftCodeResponseDto>> fetchDraftCode(@PathVariable String userId, @PathVariable String roundId, @PathVariable long questionId);
}
