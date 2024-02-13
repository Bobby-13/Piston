package com.example.CompilerApplication.api;

import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public interface CompilerRequestApi {

    @PostMapping("/compile/{id}/{round_id}/{contest_id}")
    ResponseEntity<?> ExecuteCode(@RequestBody CodeExecutionRequest request,@PathVariable String id,@PathVariable String round_id,@PathVariable String contest_id);

   @PostMapping("/submit/{id}/{round_id}/{contest_id}")
    ResponseEntity<?> SubmitCode(@RequestBody CodeExecutionRequest request,@PathVariable String id,@PathVariable String round_id,@PathVariable String contest_id);

   @GetMapping("/languages")
    ResponseEntity<?> FetchLanguages();
}
