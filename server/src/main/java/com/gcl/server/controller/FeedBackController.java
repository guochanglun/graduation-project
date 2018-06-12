package com.gcl.server.controller;

import com.gcl.server.bean.FeedBack;
import com.gcl.server.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class FeedBackController {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @PostMapping
    public @ResponseBody
    FeedBack feedback(@RequestParam("message") String message) {
        FeedBack feedBack = new FeedBack(message);
        return feedBackRepository.save(feedBack);
    }

    /**
     * 删除
     */
    @GetMapping("/all")
    public @ResponseBody
    Iterable<FeedBack> all() {
        return feedBackRepository.findAll();
    }

    /**
     * 删除
     */
    @GetMapping("/delete/{id}")
    public @ResponseBody
    int delete(@PathVariable("id") int id) {
        feedBackRepository.delete(id);
        return 1;
    }
}
