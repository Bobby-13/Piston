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

    @PostMapping("/compile/{id}/{round_id}/{contest_id}")
    ResponseEntity<CodeExecutionResponse> ExecuteCode(@RequestBody CodeExecutionRequest request, @PathVariable String id, @PathVariable String round_id, @PathVariable String contest_id);

   @PostMapping("/submit/{id}/{round_id}/{contest_id}")
    ResponseEntity<CodeExecutionResponse> SubmitCode(@RequestBody CodeExecutionRequest request,@PathVariable String id,@PathVariable String round_id,@PathVariable String contest_id);

   @GetMapping("/languages")
    ResponseEntity<List<LanguageInfoResponse>> FetchLanguages();
}
