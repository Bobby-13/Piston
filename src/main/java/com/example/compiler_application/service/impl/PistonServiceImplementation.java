package com.example.compiler_application.service.impl;

import com.example.compiler_application.dto.*;
import com.example.compiler_application.repository.CodingResultRepository;
import com.example.compiler_application.repository.service.CodingResultRepositoryImplementation;
import com.example.compiler_application.repository.service.RoundAndCodingQuestionRepositoryImplementation;
import com.example.compiler_application.service.PistonService;
import com.example.compiler_application.entity.*;
import com.example.compiler_application.util.enums.CasesType;
import com.example.compiler_application.util.enums.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class PistonServiceImplementation implements PistonService {

    private final String fileName = "Main";
    private final int compileTimeout = 0;
    private final int runTimeout = 0;
    private final int compileMemoryLimit = 0;
    private final int runMemoryLimit = 0;

    public RestTemplate restTemplate;

    private final CodingResultRepository codingResultRepository1;

    private final RoundAndCodingQuestionRepositoryImplementation roundAndCodingQuestionRepositoryImplementation;

    private final CodingResultRepositoryImplementation codingResultRepositoryImplementation;

    private final CodingResultRepository codingResultRepository;

    public PistonServiceImplementation(RestTemplate restTemplate, CodingResultRepository codingResultRepository1, RoundAndCodingQuestionRepositoryImplementation roundAndCodingQuestionRepositoryImplementation, CodingResultRepositoryImplementation codingResultRepositoryImplementation, CodingResultRepository codingResultRepository) {
        this.restTemplate = restTemplate;
        this.codingResultRepository1 = codingResultRepository1;
        this.roundAndCodingQuestionRepositoryImplementation = roundAndCodingQuestionRepositoryImplementation;
        this.codingResultRepositoryImplementation = codingResultRepositoryImplementation;
        this.codingResultRepository = codingResultRepository;
    }

    public PistonResponse compilation(CodeExecutionRequest codeExecutionRequest, CompilationRequestDto compilationRequestDto , String type)
    {
        String apiUrl = "http://127.0.0.1:2000/api/v2/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String input = "";

        if(type.equals("sample"))
        {
          input  += compilationRequestDto.getSampleInput().size()+" ";
          for(String sampleInput : compilationRequestDto.getSampleInput())
          {
              input += sampleInput+" ";
          }
        }
        else{
            input  += compilationRequestDto.getHiddenInput().size()+" ";
            for(String sampleInput : compilationRequestDto.getHiddenInput())
            {
                input += sampleInput;
            }
        }

        String code = "";
       String staticCode ="";

        for(StaticCodeDto staticCodeDto : compilationRequestDto.getStaticCodeDto()){
            if(staticCodeDto.getCodeLanguage().toString().equalsIgnoreCase(codeExecutionRequest.getLanguage())){
                staticCode = staticCodeDto.getCode();
            }
        }

        code = staticCode.replaceAll("FUNCTION",codeExecutionRequest.getCode());

        System.out.println(compilationRequestDto.getHiddenOutput());
        FileDetails fileDetails = FileDetails.builder()
                .name(fileName)
                .content(code)
                .build();

        PistonRequest pistonRequest = PistonRequest.builder()
                .files(List.of(fileDetails))
                .language(codeExecutionRequest.getLanguage())
                .version(codeExecutionRequest.getVersion())
                .stdin(input)
                .args(null)
                .compileTimeout(compileTimeout)
                .runTimeout(runTimeout)
                .compileMemoryLimit(compileMemoryLimit)
                .runMemoryLimit(runMemoryLimit)
                .build();

        HttpEntity<PistonRequest> requestEntity = new HttpEntity<>(pistonRequest, headers);

        ResponseEntity<PistonResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, PistonResponse.class);

        PistonResponse output = responseEntity.getBody();


        return output;
    }

    @Override
    public ResponseEntity<CodeExecutionResponse> codeExecution(CodeExecutionRequest codeExecutionRequest,String userId,String roundId,String contestId) {

        CompilationRequestDto compilationRequestDto = fetchCodingQuestionCases(roundId,codeExecutionRequest.getQuestionId());
        PistonResponse output = compilation(codeExecutionRequest,compilationRequestDto,"sample");

        List<String> sampleInput = compilationRequestDto.getSampleInput();
        List<String> sampleOutput = compilationRequestDto.getSampleOutput();

        Map<String,Object> map = new LinkedHashMap<>();
      try {
          ObjectMapper objectMapper = new ObjectMapper();
           map = objectMapper.readValue(output.getRun().getOutput(), Map.class);
      }
      catch (Exception e)
      {
         throw new RuntimeException();
       }

        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }
        CodingResult codingResult =fetchResult(userId,roundId);
        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(userId);
        codingResult.setContestId(contestId);
        codingResult.setRoundId(roundId);

        List<TestCasesObject> result = new ArrayList<>();

        int passCount = 0;

        for(int i=1;i<= map.size();i++){
             TestCasesObject testCasesObject = new TestCasesObject();
             String userOutput = map.get(i+"").toString();
             String exactOutput = sampleOutput.get(i-1);

             testCasesObject.setId(i+"");
             testCasesObject.setInput(sampleInput.get(i-1));
             testCasesObject.setOutput(userOutput);
             testCasesObject.setExpectedOutput(exactOutput);
             if(userOutput.equals(exactOutput)){
                 passCount++;
                 testCasesObject.setResult(Result.PASS);
             }
             else{
                 testCasesObject.setResult(Result.FAIL);
             }

             result.add(testCasesObject);
         }
         double QuestionPercentage = percentageCalculation(passCount,sampleInput.size());

        List<CodingQuestionObject> QuestionDto = null;

        if(isFirstEntry)
        {
            QuestionDto = new ArrayList<>();
        }
        else{
            QuestionDto = codingResult.getQuestion();
        }

        double totalMarks = 0;
        boolean isQuestion = false;
        if(!QuestionDto.isEmpty()){
                for (CodingQuestionObject question : QuestionDto) {
                    if (codeExecutionRequest.getQuestionId() == question.getQuestionId()) {
                        isQuestion = true;
                        question.setCode(codeExecutionRequest.getCode());
                        question.setLanguage(codeExecutionRequest.getLanguage());
                        question.setTestCases(result);
                        question.setScore(QuestionPercentage);
                        if(totalMarks==0){
                            totalMarks = question.getScore();
                        }
                        else {
                            totalMarks = percentageAverage(totalMarks, question.getScore());
                        }
                    }
                    else{
                        if(totalMarks==0){
                            totalMarks = question.getScore();
                        }
                        else {
                            totalMarks = percentageAverage(totalMarks, question.getScore());
                        }
                    }
                }
        }
        else{
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionObject);
        }

        if(!isQuestion && !isFirstEntry)
        {
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionObject);
        }

        codingResult.setQuestion(QuestionDto);
        codingResult.setTotalMarks(roundToTwoDecimalPlaces(totalMarks));

        codingResultRepository.save(codingResult);

        return ResponseEntity.ok(CodeExecutionResponse.builder().message("Executed SuccessFully").testCase(result).build());
    }


    @Override
    public ResponseEntity<CodeExecutionResponse> codeSubmission(CodeExecutionRequest codeExecutionRequest,String userId,String roundId,String contestId) {

        CompilationRequestDto compilationRequestDto = fetchCodingQuestionCases(roundId,codeExecutionRequest.getQuestionId());
        PistonResponse output = compilation(codeExecutionRequest,compilationRequestDto,"hidden");

        List<String> hiddenInput = compilationRequestDto.getHiddenInput();
        List<String> hiddenOutput = compilationRequestDto.getHiddenOutput();

        Map<String,Object> map = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(output.getRun().getOutput(), Map.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error Occur on Reading Value");
        }


        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }

        CodingResult codingResult =fetchResult(userId,roundId);

        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(userId);
        codingResult.setContestId(contestId);
        codingResult.setRoundId(roundId);

        List<HiddenTestCaseDto> hiddenTestCaseDto = new ArrayList<>();


        List<TestCasesObject> result = new ArrayList<>();

        int passCount = 0;
        for(int i=1;i<= map.size();i++){

            TestCasesObject testCasesObject = new TestCasesObject();
            String userOutput= map.get(i+"").toString();
            String exactOutput = hiddenOutput.get(i-1);

            testCasesObject.setId(i+"");
            testCasesObject.setInput(hiddenInput.get(i-1));
            testCasesObject.setOutput(userOutput);
            testCasesObject.setExpectedOutput(exactOutput);
            if(userOutput.equals(exactOutput)){
                passCount++;
                testCasesObject.setResult(Result.PASS);
            }
            else{
                testCasesObject.setResult(Result.FAIL);
            }
            result.add(testCasesObject);

            hiddenTestCaseDto.add(HiddenTestCaseDto.builder().id(i+"").result(testCasesObject.getResult()).build());
        }
        double questionPercentage = percentageCalculation(passCount, hiddenInput.size());

        List<CodingQuestionObject> QuestionDto = null;

        if(isFirstEntry)
        {
            QuestionDto = new ArrayList<>();
        }
        else{
            QuestionDto = codingResult.getQuestion();
        }

        double totalMarks = 0;
        boolean isQuestion = false;
        if(!QuestionDto.isEmpty()){
            for (CodingQuestionObject question : QuestionDto) {
                if (codeExecutionRequest.getQuestionId() == question.getQuestionId()) {
                    isQuestion = true;
                    question.setCode(codeExecutionRequest.getCode());
                    question.setLanguage(codeExecutionRequest.getLanguage());
                    question.setTestCases(result);
                    question.setScore(questionPercentage);
                    if(totalMarks==0){
                        totalMarks = question.getScore();
                    }
                    else {
                        totalMarks = percentageAverage(totalMarks, question.getScore());
                    }
                }
                else{
                    if(totalMarks==0){
                        totalMarks = question.getScore();
                    }
                    else {
                        totalMarks = percentageAverage(totalMarks, question.getScore());
                    }
                }
            }
        }
        else{
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(questionPercentage).build();
            QuestionDto.add(codingQuestionObject);
        }

        if(!isQuestion && !isFirstEntry)
        {
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(questionPercentage).build();
            QuestionDto.add(codingQuestionObject);
        }

        codingResult.setQuestion(QuestionDto);
        codingResult.setTotalMarks(roundToTwoDecimalPlaces(totalMarks));

        codingResultRepository.save(codingResult);

        return ResponseEntity.ok(CodeExecutionResponse.builder().message("Submitted Successfully").testCase(hiddenTestCaseDto).build());

    }

    @Override
    public ResponseEntity<List<LanguageInfoResponse>> fetchLanguages() {
        String apiUrl = "http://127.0.0.1:2000/api/v2/runtimes";

        LanguageInfoDto[] languageInfoDto = restTemplate.getForObject(apiUrl, LanguageInfoDto[].class);
        List<LanguageInfoResponse> response = new LinkedList<>();

        for(int i=0;i<languageInfoDto.length;i++){
             response.add(LanguageInfoResponse.builder().language(languageInfoDto[i].getLanguage()).version(languageInfoDto[i].getVersion()).build());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<QuestionDto>> fetchCodingQuestion(String roundId) {

       List<CodingQuestion> codingQuestionList = roundAndCodingQuestionRepositoryImplementation.getCodingQuestion(roundId);

       List<QuestionDto> questionDtoList = new ArrayList<>();
       for(CodingQuestion codingQuestion : codingQuestionList)
       {
           QuestionDto questionDto = new QuestionDto();

           questionDto.setQuestionId(codingQuestion.getQuestionId());
           questionDto.setQuestion(codingQuestion.getQuestion());
           questionDto.setImageUrl(codingQuestion.getImageUrl());
           questionDto.setCategory(codingQuestion.getCategory().getQuestionCategory().toString().replaceAll("_.*",""));
           questionDto.setDifficulty(codingQuestion.getDifficulty());

           List<Cases> caseDto = codingQuestion.getCasesList();

           List<CaseDto> caseDtoList =new ArrayList<>();
           for (Cases cases:caseDto){
              if(cases.getCasesType() == CasesType.SAMPLE){
                  caseDtoList.add(CaseDto.builder()
                          .caseId(cases.getCaseId())
                          .input(cases.getInput())
                          .output(cases.getOutput())
                          .build());
              }

           }

           questionDto.setCasesList(caseDtoList);

           List<FunctionCode> functionCodes = codingQuestion.getFunctionCodes();

           List<FunctionCodeDto> functionCodeDtoList = new ArrayList<>();
           for (FunctionCode functionCode:functionCodes){
               functionCodeDtoList.add(FunctionCodeDto.builder()
                               .functionCodeId(functionCode.getFunctionCodeId())
                               .code(functionCode.getCode())
                               .codeLanguage(functionCode.getCodeLanguage())
                       .build());
           }

           questionDto.setFunctionCodes(functionCodeDtoList);

           questionDtoList.add(questionDto);
       }
        return ResponseEntity.ok(questionDtoList);
    }


    @Override
    public ResponseEntity<List<DraftCodeResponseDto>> fetchDraftCode(String userId, String roundId, long questionId) {

        CodingResult codingResult = codingResultRepository1.findByUserIdAndRoundId(userId, roundId);

        List<DraftCodeResponseDto> draftCodeResponseDtos = new ArrayList<>();

        for(CodingQuestionObject codingQuestion : codingResult.getQuestion())
        {
            draftCodeResponseDtos.add(DraftCodeResponseDto.builder()
                            .questionId(codingQuestion.getQuestionId())
                            .language(codingQuestion.getLanguage())
                            .code(codingQuestion.getCode())
                    .build());
        }
        return ResponseEntity.ok(draftCodeResponseDtos);
    }


    public CompilationRequestDto fetchCodingQuestionCases(String roundId,long questionId){
        CodingQuestion codingQuestion = roundAndCodingQuestionRepositoryImplementation.getStaticCodeAndTestCases(roundId,questionId);

        StaticCodeDto.builder().staticCodeId(codingQuestion.getStaticCodes().get(0).getStaticCodeId()).build();

        List<String>  sampleTestCase = new ArrayList<>();
        List<String>  hiddenTestCase = new ArrayList<>();

        List<String> sampleTestCaseOutput = new ArrayList<>();
        List<String> hiddenTestCaseOutput = new ArrayList<>();

        for (Cases cases:codingQuestion.getCasesList()) {
            if (cases.getCasesType() == CasesType.SAMPLE) {
                sampleTestCase.add(cases.getInput());
                sampleTestCaseOutput.add(cases.getOutput());
            }
            else{
                hiddenTestCaseOutput.add(cases.getOutput());
                hiddenTestCase.add(cases.getInput());
            }
        }

        List<StaticCodeDto> staticCodeDtos = new ArrayList<>();

        for(StaticCode staticCode : codingQuestion.getStaticCodes()){
            staticCodeDtos.add(StaticCodeDto.builder()
                            .staticCodeId(staticCode.getStaticCodeId())
                            .codeLanguage(staticCode.getCodeLanguage())
                            .code(staticCode.getCode())
                    .build());
        }
     return CompilationRequestDto.builder()
             .sampleInput(sampleTestCase)
             .hiddenInput(hiddenTestCase)
             .sampleOutput(sampleTestCaseOutput)
             .hiddenOutput(hiddenTestCaseOutput)
             .staticCodeDto(staticCodeDtos)
             .build();
    }

    public double percentageCalculation(int count,int total){
        double percentage = count*100/(total * 1.0);
        return roundToTwoDecimalPlaces(percentage);
    }

    public static double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public CodingResult fetchResult(String userId,String roundId){
       return codingResultRepositoryImplementation.findByUserIdAndRoundId(userId,roundId);
    }

    public double percentageAverage(double val1,double val2){
        return (val1 + val2)/2;
    }

}
