package cl.services.lti1p3.ag.receiver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cl.services.lti1p3.ag.receiver.dto.Score;
import cl.services.lti1p3.ag.receiver.service.AGService;

@RestController
public class AGController {
    
	@Autowired
	private AGService agService;
    

    @RequestMapping(
    		path= "/ag/{param1}/lineitems/{param2}/scores",
    		method = RequestMethod.POST,
    		consumes = {MediaType.APPLICATION_JSON_VALUE,"application/vnd.ims.lis.v1.score+json"},
    		produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void scores(@PathVariable String param1,@PathVariable String param2, @RequestBody Score scoreDTO) {
    	agService.postScores(scoreDTO);
    }
    
  
    
    
}
