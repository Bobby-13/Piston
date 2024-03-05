package com.example.compiler_application.service.impl;

import com.example.compiler_application.dto.*;
import com.example.compiler_application.entity.*;
import com.example.compiler_application.repository.CodingResultRepository;
import com.example.compiler_application.repository.service.CodingResultRepositoryImplementation;
import com.example.compiler_application.repository.service.RoundAndCodingQuestionRepositoryImplementation;
import com.example.compiler_application.repository.service.RoundsRepositoryImplementation;
import com.example.compiler_application.repository.service.UserRepositoryImplementation;
import com.example.compiler_application.service.PistonService;
import com.example.compiler_application.util.enums.CasesType;
import com.example.compiler_application.util.enums.Difficulty;
import com.example.compiler_application.util.enums.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@RequiredArgsConstructor
public class PistonServiceImplementation implements PistonService {

    private final RestTemplate restTemplate;

    private final RoundsRepositoryImplementation roundsRepositoryImplementation;

    private final RoundAndCodingQuestionRepositoryImplementation roundAndCodingQuestionRepositoryImplementation;

    private final CodingResultRepositoryImplementation codingResultRepositoryImplementation;

    private final CodingResultRepository codingResultRepository;

    private final UserRepositoryImplementation userRepositoryImplementation;

    public PistonResponse compilation(CodeExecutionRequest codeExecutionRequest, CompilationRequestDto compilationRequestDto, String type) {
        String apiUrl = "http://127.0.0.1:2000/api/v2/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        StringBuilder input = new StringBuilder();

        if (type.equals("sample")) {
            input.append(compilationRequestDto.getSampleInput().size()).append(" ");
            for (String sampleInput : compilationRequestDto.getSampleInput()) {
                input.append(sampleInput).append(" ");
            }
        } else {
            input.append(compilationRequestDto.getHiddenInput().size()).append(" ");
            for (String sampleInput : compilationRequestDto.getHiddenInput()) {
                input.append(sampleInput);
            }
        }

        String code = "";
        String staticCode = "";

        for (StaticCodeDto staticCodeDto : compilationRequestDto.getStaticCodeDto()) {
            if (staticCodeDto.getCodeLanguage().toString().equalsIgnoreCase(codeExecutionRequest.getLanguage())) {
                staticCode = staticCodeDto.getCode();
            }
        }

        code = staticCode.replaceAll("FUNCTION", codeExecutionRequest.getCode());

        FileDetails fileDetails = FileDetails.builder()
                .name("Main")
                .content(code)
                .build();

        PistonRequest pistonRequest = PistonRequest.builder()
                .files(List.of(fileDetails))
                .language(codeExecutionRequest.getLanguage())
                .version(codeExecutionRequest.getVersion())
                .stdin(input.toString())
                .args(null)
                .compileTimeout(0)
                .runTimeout(0)
                .compileMemoryLimit(0)
                .runMemoryLimit(0)
                .build();

        HttpEntity<PistonRequest> requestEntity = new HttpEntity<>(pistonRequest, headers);

        ResponseEntity<PistonResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, PistonResponse.class);

        return responseEntity.getBody();
    }


    public Map<String, Object> userOutput(PistonResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getRun().getOutput(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error Occur on Reading Value");
        }

    }

    public TestCaseEvaluationDto testCaseEvaluation(Map<String, Object> map, CompilationRequestDto compilationRequestDto) {

        List<String> sampleInput = compilationRequestDto.getSampleInput();
        List<String> sampleOutput = compilationRequestDto.getSampleOutput();

        List<TestCasesObject> result = new ArrayList<>();

        int passCount = 0;

        for (int i = 1; i <= map.size(); i++) {
            TestCasesObject testCasesObject = new TestCasesObject();
            String userOutput = map.get(i + "").toString();
            String exactOutput = sampleOutput.get(i - 1);

            testCasesObject.setId(i + "");
            testCasesObject.setInput(sampleInput.get(i - 1));
            testCasesObject.setOutput(userOutput);
            testCasesObject.setExpectedOutput(exactOutput);
            if (userOutput.equals(exactOutput)) {
                passCount++;
                testCasesObject.setResult(Result.PASS);
            } else {
                testCasesObject.setResult(Result.FAIL);
            }
            result.add(testCasesObject);
        }

        return TestCaseEvaluationDto.builder()
                .testCases(result)
                .passPercentage(percentageCalculation(passCount, sampleInput.size()))
                .passCount(passCount)
                .testCaseCount(sampleInput.size())
                .build();
    }

    public QuestionEvaluationDto codingQuestionEvaluation(List<CodingQuestionObject> questionDto, CodeExecutionRequest codeExecutionRequest, TestCaseEvaluationDto testCaseEvaluation, CompilationRequestDto compilationRequestDto, Map<Difficulty, Integer> difficultyPercentage) {

        int totalMarks = 0;
        boolean isQuestion = false;
        QuestionCredentialsDto questionCredentialsDto = getTotalMarks(questionDto, codeExecutionRequest, testCaseEvaluation, compilationRequestDto, isQuestion, totalMarks, difficultyPercentage);


        return QuestionEvaluationDto.builder()
                .questionObjectList(questionDto)
                .totalMarks(questionCredentialsDto.getTotalMarks())
                .questionDifficulty(questionCredentialsDto.getQuestionDifficulty())
                .build();
    }

    private QuestionCredentialsDto getTotalMarks(List<CodingQuestionObject> questionDto, CodeExecutionRequest codeExecutionRequest, TestCaseEvaluationDto testCaseEvaluation, CompilationRequestDto compilationRequestDto, boolean isQuestion, int totalMarks, Map<Difficulty, Integer> difficultyPercentage) {

        if (!questionDto.isEmpty()) {
            for (CodingQuestionObject question : questionDto) {
                if (codeExecutionRequest.getQuestionId() == question.getQuestionId()) {

                    isQuestion = true;
                    question.setCode(codeExecutionRequest.getCode());
                    question.setLanguage(codeExecutionRequest.getLanguage());
                    question.setTestCases(testCaseEvaluation.getTestCases());
                    question.setScore(testCaseEvaluation.getPassPercentage());
                    question.setPassCount(testCaseEvaluation.getPassCount());
                    question.setTestCaseCount(testCaseEvaluation.getTestCaseCount());
                    question.setDifficulty(compilationRequestDto.getDifficulty());
                    question.setQuestionCategory(compilationRequestDto.getQuestionCategory());

                    if (totalMarks == 0) {
                        difficultyPercentage.put(question.getDifficulty(), question.getScore());
                        totalMarks = question.getScore();
                    } else {
                        difficultyPercentage.put(question.getDifficulty(), percentageAverage(difficultyPercentage.get(question.getDifficulty()), question.getScore()));
                        totalMarks = percentageAverage(totalMarks, question.getScore());
                    }
                } else {
                    if (totalMarks == 0) {
                        difficultyPercentage.put(question.getDifficulty(), question.getScore());
                        totalMarks = question.getScore();
                    } else {
                        difficultyPercentage.put(question.getDifficulty(), percentageAverage(difficultyPercentage.get(question.getDifficulty()), question.getScore()));
                        totalMarks = percentageAverage(totalMarks, question.getScore());
                    }
                }
            }
        } else {
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(testCaseEvaluation.getTestCases()).score(testCaseEvaluation.getPassPercentage()).testCaseCount(testCaseEvaluation.getTestCaseCount()).passCount(testCaseEvaluation.getPassCount()).build();
            questionDto.add(codingQuestionObject);
            difficultyPercentage.put(compilationRequestDto.getDifficulty(), testCaseEvaluation.getPassPercentage());
            totalMarks = testCaseEvaluation.getPassPercentage();
        }
        if (!isQuestion && questionDto.isEmpty()) {
            CodingQuestionObject codingQuestionObject = CodingQuestionObject.builder().questionId(codeExecutionRequest.getQuestionId()).code(codeExecutionRequest.getCode()).testCases(testCaseEvaluation.getTestCases()).score(testCaseEvaluation.getPassPercentage()).testCaseCount(testCaseEvaluation.getTestCaseCount()).passCount(testCaseEvaluation.getPassCount()).build();
            questionDto.add(codingQuestionObject);
            difficultyPercentage.put(compilationRequestDto.getDifficulty(), testCaseEvaluation.getPassPercentage());
            totalMarks = testCaseEvaluation.getPassPercentage();
        }

        return QuestionCredentialsDto.builder()
                .totalMarks(totalMarks)
                .questionDifficulty(difficultyPercentage)
                .build();
    }

    @Override
    public ResponseEntity<ResponseDto> codeExecution(CodeExecutionRequest codeExecutionRequest, String userId, String roundId, String contestId) {
        try {
            CompilationRequestDto compilationRequestDto = fetchCodingQuestionCases(roundId, codeExecutionRequest.getQuestionId());

            PistonResponse output = compilation(codeExecutionRequest, compilationRequestDto, "sample");

            Map<String, Object> map = userOutput(output);

            if (output.getRun().getSignal() != null) {
                String err = output.getRun().getStderr();
                return ResponseEntity.ok(ResponseDto.builder().message(err).object(null).build());
            }
            CodingResult codingResult = fetchResult(userId, roundId);

            List<CodingQuestionObject> questionDto = null;
            if (codingResult == null) {
                questionDto = new ArrayList<>();
                codingResult = new CodingResult();

            } else {
                questionDto = codingResult.getQuestion();
            }

            Map<Difficulty,Integer> questionDifficulty = new LinkedHashMap<>();
            codingResult.setUserId(userId);
            codingResult.setContestId(contestId);
            codingResult.setRoundId(roundId);

            TestCaseEvaluationDto testCaseEvaluation = testCaseEvaluation(map, compilationRequestDto);

            QuestionEvaluationDto questionValidation = codingQuestionEvaluation(questionDto, codeExecutionRequest, testCaseEvaluation, compilationRequestDto, questionDifficulty );

            codingResult.setQuestion(questionValidation.getQuestionObjectList());
            codingResult.setPercentage(null);
            codingResult.setResult(null);
            codingResult.setTotalScore(0);

            codingResultRepository.save(codingResult);

            return ResponseEntity.ok(ResponseDto.builder().message("Executed SuccessFully").object(testCaseEvaluation.getTestCases()).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder()
                    .message(e.toString().replaceAll(".*: ", ""))
                    .object(null)
                    .build());
        }
    }

    @Override
    public ResponseEntity<ResponseDto> codeSubmission(CodeExecutionRequest codeExecutionRequest, String userId, String roundId, String contestId) {
        try {

            List<CodingQuestion> codingQuestionList = roundAndCodingQuestionRepositoryImplementation.getCodingQuestion(roundId);

            int easy = 0;
            int medium = 0;
            int hard = 0;

            for (CodingQuestion codingQuestion : codingQuestionList) {
                if (codingQuestion.getDifficulty() == Difficulty.EASY) {
                    easy++;
                } else if (codingQuestion.getDifficulty() == Difficulty.MEDIUM) {
                    medium++;
                } else {
                    hard++;
                }
            }

            CompilationRequestDto compilationRequestDto = fetchCodingQuestionCases(roundId, codeExecutionRequest.getQuestionId());
            PistonResponse output = compilation(codeExecutionRequest, compilationRequestDto, "sample");

            Map<String, Object> map = userOutput(output);

            if (output.getRun().getSignal() != null) {
                String err = output.getRun().getStderr();
                return ResponseEntity.ok(ResponseDto.builder().message(err).object(null).build());
            }
            CodingResult codingResult = fetchResult(userId, roundId);

            List<CodingQuestionObject> questionDto = null;
            if (codingResult == null) {
                questionDto = new ArrayList<>();
                codingResult = new CodingResult();

                codingResult.setPercentage(new LinkedHashMap<>());
            } else {
                if(codingResult.getPercentage() == null){
                    LinkedHashMap<Difficulty, Integer> difficultyPercentage = new LinkedHashMap<>();
                    if (easy > 0) {
                        difficultyPercentage.put(Difficulty.EASY, 0);
                    }

                    if (medium > 0) {
                        difficultyPercentage.put(Difficulty.MEDIUM, 0);
                    }

                    if (hard > 0) {
                        difficultyPercentage.put(Difficulty.HARD, 0);
                    }
                    codingResult.setPercentage(difficultyPercentage);
                }
                questionDto = codingResult.getQuestion();
            }

            codingResult.setUserId(userId);
            codingResult.setContestId(contestId);
            codingResult.setRoundId(roundId);

            TestCaseEvaluationDto testCaseEvaluation = testCaseEvaluation(map, compilationRequestDto);

            List<HiddenTestCaseDto> hiddenTestCaseDto = new ArrayList<>();

            for (TestCasesObject testCasesObject : testCaseEvaluation.getTestCases()) {

                hiddenTestCaseDto.add(HiddenTestCaseDto.builder().id(testCasesObject.getId()).result(testCasesObject.getResult()).build());
            }


            QuestionEvaluationDto questionValidation = codingQuestionEvaluation(questionDto, codeExecutionRequest, testCaseEvaluation, compilationRequestDto, codingResult.getPercentage());

            codingResult.setQuestion(questionValidation.getQuestionObjectList());

            codingResult.setPercentage(questionValidation.getQuestionDifficulty());

            if (compilationRequestDto.getDifficulty().equals(Difficulty.EASY)) {
                codingResult.setTotalScore(totalScoreEvaluation(easy, medium, hard, questionValidation.getTotalMarks()));
            } else if (compilationRequestDto.getDifficulty().equals(Difficulty.MEDIUM)) {
                codingResult.setTotalScore(totalScoreEvaluation(easy, medium, hard, questionValidation.getTotalMarks() * 2));
            } else {
                codingResult.setTotalScore(totalScoreEvaluation(easy, medium, hard, questionValidation.getTotalMarks() * 3));
            }


            codingResult.setResult(codingRoundResult(userId, roundId, questionValidation.getTotalMarks()));

            codingResultRepository.save(codingResult);

            return ResponseEntity.ok(ResponseDto.builder().message("Submitted Successfully").object(hiddenTestCaseDto).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder()
                    .message(e.toString().replaceAll(".*: ", ""))
                    .object(null)
                    .build());
        }
    }

    @Override
    public ResponseEntity<ResponseDto> fetchLanguages() {
        String apiUrl = "http://127.0.0.1:2000/api/v2/runtimes";

        LanguageInfoDto[] languageInfoDto = restTemplate.getForObject(apiUrl, LanguageInfoDto[].class);
        List<LanguageInfoResponse> response = new LinkedList<>();

        if (languageInfoDto != null) {
            for (LanguageInfoDto infoDto : languageInfoDto) {
                response.add(LanguageInfoResponse.builder().language(infoDto.getLanguage()).version(infoDto.getVersion()).build());
            }
        }

        return ResponseEntity.ok(ResponseDto.builder()
                .object(response)
                .message("Languages Fetched SuccessFully")
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> fetchCodingQuestion(String roundId) {
        List<CodingQuestion> codingQuestionList = roundAndCodingQuestionRepositoryImplementation.getCodingQuestion(roundId);

        if (codingQuestionList.isEmpty()) {
            return ResponseEntity.ok(ResponseDto.builder()
                    .message("No Question Assigned for the Contest")
                    .object(null)
                    .build());
        }

        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (CodingQuestion codingQuestion : codingQuestionList) {
            QuestionDto questionDto = new QuestionDto();

            questionDto.setQuestionId(codingQuestion.getQuestionId());
            questionDto.setQuestion(codingQuestion.getQuestion());
            questionDto.setImageUrl(codingQuestion.getImageUrl());
            questionDto.setCategory(codingQuestion.getCategory().getQuestionCategory().toString().replaceAll("_.*", ""));
            questionDto.setDifficulty(codingQuestion.getDifficulty());

            List<Cases> caseDto = codingQuestion.getCasesList();

            List<CaseDto> caseDtoList = new ArrayList<>();
            for (Cases cases : caseDto) {
                if (cases.getCasesType() == CasesType.SAMPLE) {
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
            for (FunctionCode functionCode : functionCodes) {
                functionCodeDtoList.add(FunctionCodeDto.builder()
                        .functionCodeId(functionCode.getFunctionCodeId())
                        .code(functionCode.getCode())
                        .codeLanguage(functionCode.getCodeLanguage())
                        .build());
            }

            questionDto.setFunctionCodes(functionCodeDtoList);

            questionDtoList.add(questionDto);
        }
        return ResponseEntity.ok(ResponseDto.builder()
                .object(questionDtoList)
                .message("Question Fetched SuccessFully")
                .build());
    }


    @Override
    public ResponseEntity<ResponseDto> fetchDraftCode(String userId, String roundId, long questionId) {

        CodingResult codingResult = codingResultRepository.findByUserIdAndRoundId(userId, roundId);

        if (codingResult == null) {
            return ResponseEntity.ok(ResponseDto.builder()
                    .object(null)
                    .message("No Data Found")
                    .build());
        }

        List<DraftCodeResponseDto> draftCodeResponseDtos = new ArrayList<>();

        for (CodingQuestionObject codingQuestion : codingResult.getQuestion()) {
            draftCodeResponseDtos.add(DraftCodeResponseDto.builder()
                    .questionId(codingQuestion.getQuestionId())
                    .language(codingQuestion.getLanguage())
                    .code(codingQuestion.getCode())
                    .build());
        }
        return ResponseEntity.ok(ResponseDto.builder()
                .object(draftCodeResponseDtos)
                .message("Data Found")
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> updateResult(String roundId, int passMark) {

        List<CodingResult> codingResultList = codingResultRepositoryImplementation.findByRoundId(roundId);

        for (CodingResult codingResult : codingResultList) {

            User user = userRepositoryImplementation.getUserById(codingResult.getUserId());

            if (codingResult.getTotalScore() >= passMark) {
                user.setPassed(true);
                userRepositoryImplementation.updateUserByResult(user);
                codingResult.setResult(Result.PASS);
            } else {
                user.setPassed(false);
                userRepositoryImplementation.updateUserByResult(user);
                codingResult.setResult(Result.FAIL);
            }

            codingResultRepository.save(codingResult);
        }
        List<CodingResult> codingResultList1 = codingResultRepositoryImplementation.findByRoundId(roundId);

        return ResponseEntity.ok(ResponseDto.builder()
                .message("")
                .object(codingResultList1)
                .build());
    }


    public CompilationRequestDto fetchCodingQuestionCases(String roundId, long questionId) {

        CodingQuestion codingQuestion = roundAndCodingQuestionRepositoryImplementation.getStaticCodeAndTestCases(roundId, questionId);

        if (codingQuestion == null) {
            throw new RuntimeException("Resource Not Found");
        }

        StaticCodeDto.builder().staticCodeId(codingQuestion.getStaticCodes().get(0).getStaticCodeId()).build();

        List<String> sampleTestCase = new ArrayList<>();
        List<String> hiddenTestCase = new ArrayList<>();

        List<String> sampleTestCaseOutput = new ArrayList<>();
        List<String> hiddenTestCaseOutput = new ArrayList<>();

        for (Cases cases : codingQuestion.getCasesList()) {
            if (cases.getCasesType() == CasesType.SAMPLE) {
                sampleTestCase.add(cases.getInput());
                sampleTestCaseOutput.add(cases.getOutput());
            } else {
                hiddenTestCaseOutput.add(cases.getOutput());
                hiddenTestCase.add(cases.getInput());
            }
        }

        List<StaticCodeDto> staticCodeDtos = new ArrayList<>();

        for (StaticCode staticCode : codingQuestion.getStaticCodes()) {
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
                .difficulty(codingQuestion.getDifficulty())
                .questionCategory(codingQuestion.getCategory().getQuestionCategory())
                .build();
    }

    public int percentageCalculation(int count, int total) {
        double percentage = count * 100 / (total * 1.0);
        return (int) Math.round(percentage);
    }

    public double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public CodingResult fetchResult(String userId, String roundId) {
        return codingResultRepositoryImplementation.findByUserIdAndRoundId(userId, roundId);
    }

    public int percentageAverage(int val1, int val2) {
        return (int) Math.round((val1 + val2) / 2.0);
    }


    public Result codingRoundResult(String userId, String roundId, double total) {

        int passValue = roundsRepositoryImplementation.getPassMark(roundId);

        User user = userRepositoryImplementation.getUserById(userId);

        if (total >= passValue) {
            user.setPassed(true);
            userRepositoryImplementation.updateUserByResult(user);
            return Result.PASS;
        } else {
            user.setPassed(false);
            userRepositoryImplementation.updateUserByResult(user);
            return Result.FAIL;
        }
    }

    public double totalScoreEvaluation(int easy, int medium, int hard, int marks) {

        easy = weightage(Difficulty.EASY, easy) * 100;

        medium = weightage(Difficulty.MEDIUM, medium) * 100;

        hard = weightage(Difficulty.HARD, hard) * 100;

        double totalWeightage = (easy + medium + hard) * 1.0;

        double totalScore = (marks / totalWeightage) * 100;

        return roundToTwoDecimalPlaces(totalScore);
    }

    public int weightage(Difficulty difficulty, int score) {
        if (difficulty == Difficulty.EASY) {
            return score;
        } else if (difficulty == Difficulty.MEDIUM) {
            return 2 * score;
        } else {
            return 3 * score;
        }
    }

}
