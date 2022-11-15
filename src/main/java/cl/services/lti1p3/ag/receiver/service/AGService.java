package cl.services.lti1p3.ag.receiver.service;

import org.springframework.stereotype.Service;

import cl.services.lti1p3.ag.receiver.dto.Score;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AGService {public void postScores(Score scoreDTO) {
		log.info("Receiving scores :"+scoreDTO);
		
	}
     
}
