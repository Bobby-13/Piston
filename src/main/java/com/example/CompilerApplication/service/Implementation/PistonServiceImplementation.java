package com.example.CompilerApplication.service.Implementation;

import com.example.CompilerApplication.model.dto.*;
import com.example.CompilerApplication.model.entity.enums.CasesType;
import com.example.CompilerApplication.model.entity.enums.CodeLanguage;
import com.example.CompilerApplication.model.entity.enums.Result;
import com.example.CompilerApplication.model.entity.table.*;
import com.example.CompilerApplication.repository.CodingResultRepository;
import com.example.CompilerApplication.repository.service.CodingResultRepositoryImplementation;
import com.example.CompilerApplication.repository.service.RoundAndCodingQuestionRepositoryImplementation;
import com.example.CompilerApplication.service.PistonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@Slf4j
public class PistonServiceImplementation implements PistonService {

    private final static String filename = "Main";
    private final static int compileTimeout = 0;
    private final static int runTimeout = 0;
    private final static int compileMemoryLimit = 0;
    private final static int runMemoryLimit = 0;

    public RestTemplate restTemplate;

//    @Autowired
    private final CodingResultRepository codingResultRepository1;
//
//    public PistonServiceImplementation(CodingResultRepository codingResultRepository1) {
//        this.codingResultRepository1 = codingResultRepository1;
//    }


    @Autowired
    private RoundAndCodingQuestionRepositoryImplementation roundAndCodingQuestionRepositoryImplementation;

    @Autowired
    private CodingResultRepositoryImplementation codingResultRepositoryImplementation;

    @Autowired
    private CodingResultRepository codingResultRepository;

    public PistonServiceImplementation(RestTemplate restTemplate, CodingResultRepository codingResultRepository1) {
        this.restTemplate = restTemplate;
        this.codingResultRepository1 = codingResultRepository1;
    }

    public PistonResponse Compilation(CodeExecutionRequest codeExecutionRequest,CompilationRequestDto compilationRequestDto , String type)
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

        System.out.println(input);
        System.out.println(compilationRequestDto.getSampleOutput());

        System.out.println(compilationRequestDto.getHiddenOutput());
        FileDetails fileDetails = FileDetails.builder()
                .name(filename)
                .content(codeExecutionRequest.getCode())
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
    public ResponseEntity<CodeExecutionResponse> CodeExecution(CodeExecutionRequest codeExecutionRequest,String userId,String roundId,String contestId) {

        CompilationRequestDto compilationRequestDto = FetchCodingQuestionCases(roundId,codeExecutionRequest.getQuestionId());
        PistonResponse output = Compilation(codeExecutionRequest,compilationRequestDto,"sample");

        List<String> sampleInput = compilationRequestDto.getSampleInput();
        List<String> sampleOutput = compilationRequestDto.getSampleOutput();

//        System.out.println(output.getRun().getStdout());
//        "{\"1\":  7, \"2\": 9, \"3\": 6}"
        Map<String,Object> map = new LinkedHashMap<>();
      try {
          ObjectMapper objectMapper = new ObjectMapper();
           map = objectMapper.readValue(output.getRun().getOutput(), Map.class);
      }
      catch (Exception e)
      {
          System.out.println(e);
      }

        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }
        CodingResult codingResult =FetchResult(userId,roundId);
        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(userId);
        codingResult.setContestId(contestId);
        codingResult.setRoundId(roundId);

        List<TestCasesDto> result = new ArrayList<>();

        int passCount = 0;

        for(int i=1;i<= map.size();i++){
             TestCasesDto testCasesDto = new TestCasesDto();
             String userOutput = map.get(i+"").toString();
             String exactOutput = sampleOutput.get(i-1);

             testCasesDto.setId(i+"");
             testCasesDto.setInput(sampleInput.get(i-1));
             testCasesDto.setOutput(userOutput);
             testCasesDto.setExpectedOutput(exactOutput);
             if(userOutput.equals(exactOutput)){
                 passCount++;
                 testCasesDto.setResult(Result.PASS);
             }
             else{
                 testCasesDto.setResult(Result.FAIL);
             }

             result.add(testCasesDto);
         }
         double QuestionPercentage = PercentageCalculation(passCount,sampleInput.size());

        List<CodingQuestionDto> QuestionDto = null;

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
                for (CodingQuestionDto question : QuestionDto) {
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
                            totalMarks = PercentageAverage(totalMarks, question.getScore());
                        }
                    }
                    else{
                        if(totalMarks==0){
                            totalMarks = question.getScore();
                        }
                        else {
                            totalMarks = PercentageAverage(totalMarks, question.getScore());
                        }
                    }
                }
        }
        else{
            CodingQuestionDto codingQuestionDto = CodingQuestionDto.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionDto);
        }

        if(!isQuestion && !isFirstEntry)
        {
            CodingQuestionDto codingQuestionDto = CodingQuestionDto.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionDto);
        }

        codingResult.setQuestion(QuestionDto);
        codingResult.setTotalMarks(RoundToTwoDecimalPlaces(totalMarks));

        codingResultRepository.save(codingResult);

        return ResponseEntity.ok(CodeExecutionResponse.builder().message("Executed SuccessFully").testCase(result).build());
    }


    @Override
    public ResponseEntity<CodeExecutionResponse> CodeSubmission(CodeExecutionRequest codeExecutionRequest,String userId,String roundId,String contestId) {

        CompilationRequestDto compilationRequestDto = FetchCodingQuestionCases(roundId,codeExecutionRequest.getQuestionId());
        PistonResponse output = Compilation(codeExecutionRequest,compilationRequestDto,"hidden");

        List<String> hiddenInput = compilationRequestDto.getHiddenInput();
        List<String> hiddenOutput = compilationRequestDto.getHiddenOutput();

        Map<String,Object> map = new LinkedHashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(output.getRun().getOutput(), Map.class);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            System.out.println(err);
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }

        CodingResult codingResult =FetchResult(userId,roundId);

        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(userId);
        codingResult.setContestId(contestId);
        codingResult.setRoundId(roundId);

        List<HiddenTestCaseDto> hiddenTestCaseDto = new ArrayList<>();


        List<TestCasesDto> result = new ArrayList<>();

        int passCount = 0;
        for(int i=1;i<= map.size();i++){

            TestCasesDto testCasesDto = new TestCasesDto();
            String userOutput= map.get(i+"").toString();
            String exactOutput = hiddenOutput.get(i-1);

            testCasesDto.setId(i+"");
            testCasesDto.setInput(hiddenInput.get(i-1));
            testCasesDto.setOutput(userOutput);
            testCasesDto.setExpectedOutput(exactOutput);
            if(userOutput.equals(exactOutput)){
                passCount++;
                testCasesDto.setResult(Result.PASS);
            }
            else{
                testCasesDto.setResult(Result.FAIL);
            }
            result.add(testCasesDto);

            hiddenTestCaseDto.add(HiddenTestCaseDto.builder().id(i+"").result(testCasesDto.getResult()).build());
        }
        double QuestionPercentage = PercentageCalculation(passCount, hiddenInput.size());

        List<CodingQuestionDto> QuestionDto = null;

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
            for (CodingQuestionDto question : QuestionDto) {
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
                        totalMarks = PercentageAverage(totalMarks, question.getScore());
                    }
                }
                else{
                    if(totalMarks==0){
                        totalMarks = question.getScore();
                    }
                    else {
                        totalMarks = PercentageAverage(totalMarks, question.getScore());
                    }
                }
            }
        }
        else{
            CodingQuestionDto codingQuestionDto = CodingQuestionDto.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionDto);
        }

        if(!isQuestion && !isFirstEntry)
        {
            CodingQuestionDto codingQuestionDto = CodingQuestionDto.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(result).score(QuestionPercentage).build();
            QuestionDto.add(codingQuestionDto);
        }

        codingResult.setQuestion(QuestionDto);
        codingResult.setTotalMarks(RoundToTwoDecimalPlaces(totalMarks));

        codingResultRepository.save(codingResult);

        return ResponseEntity.ok(CodeExecutionResponse.builder().message("Submitted Successfully").testCase(hiddenTestCaseDto).build());

    }

    @Override
    public ResponseEntity<List<LanguageInfoResponse>> FetchLanguages() {
        String apiUrl = "http://127.0.0.1:2000/api/v2/runtimes";

        LanguageInfoDto[] languageInfoDto = restTemplate.getForObject(apiUrl, LanguageInfoDto[].class);
        List<LanguageInfoResponse> response = new LinkedList<>();

        for(int i=0;i<languageInfoDto.length;i++){
             response.add(LanguageInfoResponse.builder().language(languageInfoDto[i].getLanguage()).version(languageInfoDto[i].getVersion()).build());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> FetchCodingQuestion(String roundId) {

       List<CodingQuestion> codingQuestionList = roundAndCodingQuestionRepositoryImplementation.getCodingQuestion(roundId);

       List<QuestionDto> questionDtoList = new ArrayList<>();
       for(CodingQuestion codingQuestion : codingQuestionList)
       {
           QuestionDto questionDto = new QuestionDto();

           questionDto.setQuestionId(codingQuestion.getQuestionId());
           questionDto.setQuestion(codingQuestion.getQuestion());
           questionDto.setImageUrl(codingQuestion.getImageUrl());
           questionDto.setCategory(codingQuestion.getCategory().getCategory().toString().replaceAll("_.*",""));
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
    public ResponseEntity<?> FetchDraftCode(String userId, String roundId, long questionId) {

        CodingResult codingResult = codingResultRepository1.findByUserIdAndRoundId(userId, roundId);

        List<DraftCodeResponseDto> draftCodeResponseDtos = new ArrayList<>();

        for(CodingQuestionDto codingQuestion : codingResult.getQuestion())
        {
            draftCodeResponseDtos.add(DraftCodeResponseDto.builder()
                            .questionId(codingQuestion.getQuestionId())
                            .language(codingQuestion.getLanguage())
                            .code(codingQuestion.getCode())
                    .build());
        }
        return ResponseEntity.ok(draftCodeResponseDtos);
    }


    public CompilationRequestDto FetchCodingQuestionCases(String roundId,long questionId){
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

    public double PercentageCalculation(int count,int total){
        double percentage = count*100/(total * 1.0);
        return RoundToTwoDecimalPlaces(percentage);
    }

    public static double RoundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public CodingResult FetchResult(String userId,String round_id){
       return codingResultRepositoryImplementation.findByUserIdAndRoundId(userId,round_id);
    }

    public double PercentageAverage(double val1,double val2){
        return (val1 + val2)/2;
    }

 public CodeLanguage Language(String language)
 {
     switch (language){
         case "java":
             return CodeLanguage.JAVA;
         case "c":
             return CodeLanguage.C;
         case "c++":
             return CodeLanguage.CPP;
         case "python":
             return CodeLanguage.PYTHON;
         default:
             return null;
     }
 }
}
