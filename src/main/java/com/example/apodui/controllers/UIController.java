package com.example.apodui.controllers;

import com.example.apodui.dto.ApodData;
import com.example.apodui.services.ApodDataRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UIController {

    private final ApodDataRetriever dataRetriever;

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public String badResponseFromNasa(){
        return "nonsensical_date_response";
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public String badDateFormatHandler(){
        return "bad_date_response";
    }

    @GetMapping("/picture")
    public String getApodData(@RequestParam(name = "date") String stringDate, Model model) throws NoSuchElementException {

        LocalDate date = LocalDate.parse(stringDate);
        Optional<ApodData> receivedData = dataRetriever.getApodData(date).blockOptional();
        model.addAttribute("data", receivedData.get());
        return "image_response";
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
