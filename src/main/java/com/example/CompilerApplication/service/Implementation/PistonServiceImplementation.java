package com.example.CompilerApplication.service.Implementation;

import com.example.CompilerApplication.model.dto.*;
import com.example.CompilerApplication.model.entity.enums.Result;
import com.example.CompilerApplication.model.entity.table.CodingResult;
import com.example.CompilerApplication.repository.CodingResultRepository;
import com.example.CompilerApplication.repository.service.Implementation.CodingResultRepositoryImplementation;
import com.example.CompilerApplication.service.PistonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;


@Service
@Slf4j
public class PistonServiceImplementation implements PistonService {

    private final static String filename = "Main";
    public RestTemplate restTemplate;

    @Autowired
    private CodingResultRepositoryImplementation codingResultRepositoryImplementation;
    @Autowired
    private CodingResultRepository codingResultRepository;

    public PistonServiceImplementation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PistonResponse Compilation(CodeExecutionRequest codeExecutionRequest)
    {
        String apiUrl = "http://127.0.0.1:2000/api/v2/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String arr[] = {"5 2\n","5 4\n","5 1\n"};
        String opt[] = {"7\n","9\n","5\n"};

        String input = "";

        for(int i=0;i< arr.length;i++){
            input+=arr[i];
        }
        FileDetails fileDetails = FileDetails.builder()
                .name(filename)
                .content(codeExecutionRequest.getCode())
                .build();

        PistonRequest pistonRequest = PistonRequest.builder()
                .files(List.of(fileDetails))
                .language(codeExecutionRequest.getLanguage())
                .version(codeExecutionRequest.getVersion())
                .stdin(arr.length+" "+input)
                .args(null)
                .compileTimeout(10000)
                .runTimeout(3000)
                .compileMemoryLimit(-1)
                .runMemoryLimit(-1)
                .build();

        HttpEntity<PistonRequest> requestEntity = new HttpEntity<>(pistonRequest, headers);

        ResponseEntity<PistonResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, PistonResponse.class);

        PistonResponse output = responseEntity.getBody();


        return output;
    }

    @Override
    public ResponseEntity<CodeExecutionResponse> CodeExecution(CodeExecutionRequest codeExecutionRequest,String id,String round_id,String contest_id) {

        String arr[] = {"5 2\n","5 4\n","5 1\n"};
        String opt[] = {"7\n","9\n","5\n"};

        PistonResponse output = Compilation(codeExecutionRequest);

        System.out.println(output.getRun().getStdout());
        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }
        CodingResult codingResult =FetchResult(id,round_id);
        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(id);
        codingResult.setContestId(contest_id);
        codingResult.setRoundId(round_id);

         String[] Values = output.getRun().getStdout().split("Testcase");

        Map<Integer,String> map = new LinkedHashMap<>();


        for(int i=0;i< Values.length;i++) {
            map.put(i,Values[i]);
        }

        List<TestCasesDto> result = new ArrayList<>();

        int passCount = 0;
         for(int i=1;i< map.size();i++){

             TestCasesDto testCasesDto = new TestCasesDto();
             String userOutput= map.get(i);
             String exactOutput = opt[i-1];

             testCasesDto.setId(i+"");
             testCasesDto.setInput(arr[i-1]);
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
         double QuestionPercentage = PercentageCalculation(passCount, arr.length);

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
                    if (codeExecutionRequest.getQuestionId().equals(question.getQuestionId())) {
                        isQuestion = true;
                        question.setCode(codeExecutionRequest.getCode());
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
    public ResponseEntity<CodeExecutionResponse> CodeSubmission(CodeExecutionRequest codeExecutionRequest,String id,String round_id,String contest_id) {
//        String arr[] = {"5 2\n","5 4\n","5 1\n","2 2\n","7 8\n","9 0\n","12 4\n","3 4\n","2 234\n","12 4\n","5 67\n"};
//
//        String opt[] = {"7\n","9\n","5\n","4\n","15\n","9\n","12\n","7\n","34\n","16\n","72\n"};

        String arr[] = {"5 2\n","5 4\n","5 1\n"};
        String opt[] = {"7\n","8\n","5\n"};

        PistonResponse output = Compilation(codeExecutionRequest);

        if(output.getRun().getSignal()!=null)
        {
            String err = output.getRun().getStderr();
            System.out.println(err);
            return ResponseEntity.ok(CodeExecutionResponse.builder().message(err).testCase(null).build());
        }

        CodingResult codingResult =FetchResult(id,round_id);

        boolean isFirstEntry = false;
        if(codingResult==null){
            isFirstEntry = true;
            codingResult = new CodingResult();
        }

        codingResult.setUserId(id);
        codingResult.setContestId(contest_id);
        codingResult.setRoundId(round_id);

        String[] Values = output.getRun().getStdout().split("Testcase");

        Map<Integer,String> map = new LinkedHashMap<>();

        List<HiddenTestCaseDto> hiddenTestCaseDto = new ArrayList<>();
        for(int i=0;i< Values.length;i++) {
            map.put(i,Values[i]);
        }

        List<TestCasesDto> result = new ArrayList<>();

        int passCount = 0;
        for(int i=1;i< map.size();i++){

            TestCasesDto testCasesDto = new TestCasesDto();
            String userOutput= map.get(i);
            String exactOutput = opt[i-1];

            testCasesDto.setId(i+"");
            testCasesDto.setInput(arr[i-1]);
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
        double QuestionPercentage = PercentageCalculation(passCount, arr.length);

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
                if (codeExecutionRequest.getQuestionId().equals(question.getQuestionId())) {
                    isQuestion = true;
                    question.setCode(codeExecutionRequest.getCode());
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
}
