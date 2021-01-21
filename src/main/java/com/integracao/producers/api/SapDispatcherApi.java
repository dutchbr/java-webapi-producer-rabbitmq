package com.integracao.producers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracao.producers.dto.Message;
import com.integracao.producers.service.AmqpService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class SapDispatcherApi {

    private  static ExecutorService asyncExecutor;
    SapDispatcherApi()
    {
        asyncExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }


    @Autowired
    private AmqpService service;

    @GetMapping(value="/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {

        return "Página Principal";

    }
    @GetMapping(value="sendtext")
    public String sendText(String text)
    {
        return text;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler(Exception.class)
    @PostMapping(value="sendsapcontent")
    public void sendText(@RequestBody Message message)
    {

        if (message==null)
        {
            return;

        }
        if (!StringUtils.hasText(message.getFila()))
        {
            return;

        }

        asyncExecutor.execute(() -> {service.sendToConsumer(message);});

    }























}
