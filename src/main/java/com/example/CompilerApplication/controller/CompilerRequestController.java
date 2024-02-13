package com.example.CompilerApplication.controller;

import com.example.CompilerApplication.api.CompilerRequestApi;
import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.service.PistonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CompilerRequestController implements CompilerRequestApi {

    private final PistonService pistonService;

    public CompilerRequestController(PistonService pistonService) {
        this.pistonService = pistonService;
    }

    @Override
    public ResponseEntity<?> ExecuteCode(CodeExecutionRequest request,String id,String round_id,String contest_id) {
        return pistonService.CodeExecution(request,id,round_id,contest_id);
    }

    @Override
    public ResponseEntity<?> SubmitCode(CodeExecutionRequest request,String id,String round_id,String contest_id) {
        return pistonService.CodeSubmission(request,id,round_id,contest_id);
    }

    @Override
    public ResponseEntity<?> FetchLanguages() {
        return pistonService.FetchLanguages();
    }

}
