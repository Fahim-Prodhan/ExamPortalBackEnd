package com.exam.controller;


import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.QuestionService;
import com.exam.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @Autowired
    private QuizService quizService;

    //add question
    @PostMapping("/")
    public ResponseEntity<Question> add(@RequestBody Question question) {
        return ResponseEntity.ok(this.service.addQuestion(question));
    }

    //update question
    @PutMapping("/")
    public ResponseEntity<Question> update(@RequestBody Question question)
    {
        return ResponseEntity.ok(this.service.updateQuestion(question));
    }

    //get all question of any quiz
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionOfQuiz(@PathVariable("qid") Long qid)
    {
//        Quiz quiz = new Quiz();
//        quiz.setqId(qid);
//        Set<Question> questionOfQuiz = this.service.getQuestionOfQuiz(quiz);
//        return ResponseEntity.ok(questionOfQuiz);

        Quiz quiz = this.quizService.getQuiz(qid);
        Set<Question> questions = quiz.getQuestions();
        List list = new ArrayList(questions);
        if(list.size()>Integer.parseInt(quiz.getNumberOfQuestions())) {
            list=list.subList(0,Integer.parseInt(quiz.getNumberOfQuestions()));
        }

        Collections.shuffle(list);
        return ResponseEntity.ok(list);

    }
    //get all question of any quiz in admin
    @GetMapping("/quiz/all/{qid}")
    public ResponseEntity<?> getQuestionOfQuizAdmin(@PathVariable("qid") Long qid)
    {
        Quiz quiz = new Quiz();
        quiz.setqId(qid);
        Set<Question> questionOfQuiz = this.service.getQuestionOfQuiz(quiz);
        return ResponseEntity.ok(questionOfQuiz);



    }

    //get single question
    @GetMapping("/{quesId}")
    public Question get(@PathVariable("quesId") Long quesId)
    {
        return this.service.getQuestion(quesId);
    }

    //delete Question
    @DeleteMapping("/{quesId}")
    public void delete(@PathVariable("quesId") Long quesId) {

        this.service.deleteQuestion(quesId);
    }


    //eval quiz
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions)
    {
        double marksGot=0;
        int correctAnswers=0;
        int  attempted=0;

        System.out.println(questions);
        for (Question q: questions){

            //single question
            Question question = this.service.get(q.getQuesId());
            if(question.getAnswer().equals(q.getGivenAnswer()))
            {
                    //correct ans
                correctAnswers++;
                double marksSingle = Double.parseDouble( questions.get(0).getQuiz().getMaxMarks())/questions.size();
                marksGot += marksSingle;
            }
            if(q.getGivenAnswer()!=null )
               {
                 attempted++;
               }

        }
        Map<String, Object> map = Map.of("marksGot",marksGot,"correctAnswers",correctAnswers,"attempted",attempted);
        return ResponseEntity.ok(map);
    }
}
