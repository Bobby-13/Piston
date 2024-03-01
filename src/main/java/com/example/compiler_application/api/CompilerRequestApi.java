package com.example.compiler_application.api;

import com.example.compiler_application.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public interface CompilerRequestApi {

    @PostMapping("/compile/{userId}/{roundId}/{contestId}")
    ResponseEntity<ResponseDto> executeCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @PostMapping("/submit/{userId}/{roundId}/{contestId}")
    ResponseEntity<ResponseDto> submitCode(@RequestBody CodeExecutionRequest request, @PathVariable String userId, @PathVariable String roundId, @PathVariable String contestId);

   @GetMapping("/languages")
    ResponseEntity<ResponseDto> fetchLanguages();

   @GetMapping("/codingQuestion/{roundId}")
    ResponseEntity<ResponseDto> fetchCodingQuestion(@PathVariable String roundId);

   @GetMapping("/draftCode/{userId}/{roundId}/{questionId}")
    ResponseEntity<ResponseDto> fetchDraftCode(@PathVariable String userId, @PathVariable String roundId, @PathVariable long questionId);


   @PutMapping("/updateResult/{roundId}")
    ResponseEntity<ResponseDto> updateResult(@PathVariable String roundId , @RequestParam(value = "passMark") int passMark);
}
