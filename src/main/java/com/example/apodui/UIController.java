package com.example.apodui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UIController {

    private final ApodDataRetriver dataRetriver;

    @GetMapping("/picture")
    public String getApodData(
            @RequestParam(name = "date")
            String stringDate) {
        LocalDate date;
        try{
            date = LocalDate.parse(stringDate);
        } catch (RuntimeException e) {
            String response =
                    """
                    <p>The format of date "%s" is incorrect or date doesn't make sense</p>
                    """;
            return String.format(response, stringDate);
        }
        Optional<ApodData> receivedData = dataRetriver
                .getApodData(date)
                .blockOptional();
        if(receivedData.isEmpty()){
            return "<p>No picture for the day: date doesn't make sense or NASA API is down</p>";
        }
        ApodData data = receivedData.get();
        String htmlResponse = """
                <h1 class="text-center text-4xl bg-blue-100 my-2">%s</h1>
                <img src="%s" alt="Something beautiful"/>
                <p mx-4 my-2>%s</p>
                """;
        return String.format(htmlResponse, data.title(), data.url(), data.explanation());
    }

    @GetMapping("/")
    public String mainPage(){
        return """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width,initial-scale=1">
                    <title>APOD</title>
                    <script src="https://cdn.tailwindcss.com"></script>
                    <script src="https://unpkg.com/htmx.org@1.9.9"></script>
                </head>
                <body class="flex flex-col justify-center items-center font-sans font-light text-center text-4xl">
                    <div id="apod-result" class="flex flex-col justify-center items-center">
                        <h1 class="bg-blue-100 text-center">A Picture Of The Day</h1>
                    </div>
                    <div class="flex flex-row justify-center items-center my-6">
                        <div class="mx-5">Enter the date: </div>
                        <input name="date"/ class="mx-5 border-2 border-solid border-blue-100 bg-gray-50 hover:bg-gray-200 border-blue-300 active:bg-gray-400">
                        <button class="mx-5" hx-get="/picture" hx-target="#apod-result" hx-swap="innerHTML" hx-include="[name='date']">
                            Find a picture of the day
                        </button>
                    </div>
                </body>
                </html>
                """;
    }


}
