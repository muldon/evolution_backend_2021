package com.github.eduoliveiradev.evolution.service;

import org.springframework.stereotype.Service;

import com.github.eduoliveiradev.evolution.dto.Score;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AGService {public void postScores(Score scoreDTO) {
		log.info("Receiving scores :"+scoreDTO);
		
	}
     
}
