package com.example.ag_grid_poc;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private SimpMessagingTemplate template;

    private final Random random = new Random();
    private final List<Trade> allTrades = new ArrayList<>();

    private static final int MAX_ROWS = 100000;

    private boolean initialSent = false;

    @GetMapping("/trade")
    public String test(){
        logger.info("Received /trade GET request");
        return "hello";
    }

    @PostConstruct
    public void init() {
        // Initial batch of 1000 trades


            List<Trade> initialTrades = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                Trade t = Trade.generateRandom();
                allTrades.add(t);
                initialTrades.add(t);
            }
            template.convertAndSend("/topic/trades", initialTrades);
            logger.info("Sent initial 1000 trades");



        initialSent = true;
    }

    @Scheduled(fixedRate = 1000)
    public void sendTrades() {
        if (WebSocketEventListener.connectedClients.get() == 0) return;

        if (!initialSent) {
            List<Trade> initialTrades = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                Trade t = Trade.generateRandom();
                allTrades.add(t);
                initialTrades.add(t);
            }
            template.convertAndSend("/topic/trades", initialTrades);
            logger.info("Sent initial 10000 trades");
            initialSent = true;
        }

        if (allTrades.size() >= MAX_ROWS) return;

        List<Trade> newTrades = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Trade t = Trade.generateRandom();
            allTrades.add(t);
            newTrades.add(t);
        }
        template.convertAndSend("/topic/trades", newTrades);
        logger.info("Sent additional 1000 trades, total: {}", allTrades.size());
    }

}
