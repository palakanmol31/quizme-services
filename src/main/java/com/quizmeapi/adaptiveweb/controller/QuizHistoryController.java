package com.quizmeapi.adaptiveweb.controller;

import com.quizmeapi.adaptiveweb.model.*;
import com.quizmeapi.adaptiveweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/quizHistory", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizHistoryController {
    private final QuizHistoryRepository quizHistoryRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizSessionRepository quizSessionRepository;

    @Autowired
    public QuizHistoryController(QuizHistoryRepository quizHistoryRepository, UserRepository userRepository, QuestionRepository questionRepository, QuizRepository quizRepository, QuestionRepository questionRepository1, QuizSessionRepository quizSessionRepository) {
        this.quizHistoryRepository = quizHistoryRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository1;
        this.quizSessionRepository = quizSessionRepository;
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public Iterable<QuizHistory> getQuizByUserId(@PathVariable("user_id") int userId) {
        User user = userRepository.findById(userId);
        return quizHistoryRepository.findAllByUser(user);
    }

    @RequestMapping(value = "/{quiz_id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public QuizHistory getQuizScore(@PathVariable("quiz_id") int quizId) {
        Iterable<Quiz> questionsAnswered = quizRepository.findAllByQuizId(quizId);
        int score = 0;
        for (Quiz quiz: questionsAnswered) {
            int questionId = quiz.getQuestion().getId();
            String userChoice = quiz.getUserChoice();
            Question question = questionRepository.findById(questionId);
            if (userChoice.equalsIgnoreCase(question.getAnswer())) {
                score++;
            }
        }
        QuizSession quizSession = quizSessionRepository.findByQuizId(quizId);
        User user = quizSession.getUser();
        QuizHistory quizHistory = new QuizHistory();
        quizHistory.setScore(score);
        quizHistory.setQuizId(quizId);
        quizHistory.setUser(user);
        quizHistoryRepository.save(quizHistory);
        return quizHistory;
    }

}