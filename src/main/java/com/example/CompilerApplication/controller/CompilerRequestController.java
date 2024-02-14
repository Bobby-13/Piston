package com.example.CompilerApplication.controller;

import com.example.CompilerApplication.api.CompilerRequestApi;
import com.example.CompilerApplication.model.dto.CodeExecutionRequest;
import com.example.CompilerApplication.model.dto.CodeExecutionResponse;
import com.example.CompilerApplication.model.dto.LanguageInfoResponse;
import com.example.CompilerApplication.service.PistonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CompilerRequestController implements CompilerRequestApi {

    private final PistonService pistonService;

    public CompilerRequestController(PistonService pistonService) {
        this.pistonService = pistonService;
    }

    @Override
    public ResponseEntity<CodeExecutionResponse> ExecuteCode(CodeExecutionRequest request, String id, String round_id, String contest_id) {
        return pistonService.CodeExecution(request,id,round_id,contest_id);
    }

    @Override
    public ResponseEntity<CodeExecutionResponse> SubmitCode(CodeExecutionRequest request,String id,String round_id,String contest_id) {
        return pistonService.CodeSubmission(request,id,round_id,contest_id);
    }

    @Override
    public ResponseEntity<List<LanguageInfoResponse>> FetchLanguages() {
        return pistonService.FetchLanguages();
    }

}
